/*
 * MyTake.org
 *
 *  Copyright 2017 by its authors.
 *  Some rights reserved. See LICENSE, https://github.com/mytake/mytake/graphs/contributors
 */
package controllers;

import static db.Tables.TAKEREACTION;

import auth.AuthUser;
import com.diffplug.common.base.Unhandled;
import com.google.inject.Binder;
import com.typesafe.config.Config;
import common.IpGetter;
import common.Time;
import db.enums.Reaction;
import db.tables.records.TakereactionRecord;
import java.util.List;
import java2ts.Routes;
import java2ts.TakeReactionJson;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.Request;
import org.jooq.DSLContext;

/**
 * - when a take is first opened, post to {@link Routes#API_TAKE_VIEW}.
 * - when a user reacts to a take, post to {@link Routes#API_TAKE_REACT}.
 */
public class TakeReaction implements Jooby.Module {
	@Override
	public void configure(Env env, Config conf, Binder binder) throws Throwable {
		// when a take is opened
		env.router().post(Routes.API_TAKE_VIEW, req -> {
			AuthUser user = AuthUser.authOpt(req).orElse(null);
			TakeReactionJson.ViewReq viewReq = req.body(TakeReactionJson.ViewReq.class);
			try (DSLContext dsl = req.require(DSLContext.class)) {
				TakeReactionJson.ViewRes viewRes = new TakeReactionJson.ViewRes();
				if (user != null) {
					List<Reaction> reactions = reactionsForUser(dsl, viewReq.take_id, user);
					viewRes.userState = userState(reactions);
					if (!reactions.contains(Reaction.view)) {
						setReaction(dsl, req, viewReq.take_id, user, Reaction.view, true);
					}
				}
				viewRes.takeState = takeState(dsl, viewReq.take_id);
				return viewRes;
			}
		});
		// when a take is reacted to (liked, bookmarked, etc)
		env.router().post(Routes.API_TAKE_REACT, req -> {
			AuthUser user = AuthUser.auth(req);
			TakeReactionJson.ReactReq reactReq = req.body(TakeReactionJson.ReactReq.class);
			try (DSLContext dsl = req.require(DSLContext.class)) {
				TakeReactionJson.ReactRes reactRes = new TakeReactionJson.ReactRes();
				List<Reaction> reactions = reactionsForUser(dsl, reactReq.take_id, user);
				for (Reaction reaction : REACTIONS_NON_VIEW) {
					boolean command = getReaction(reactReq.userState, reaction);
					if (command != reactions.contains(reaction)) {
						setReaction(dsl, req, reactReq.take_id, user, reaction, command);
					}
				}
				reactRes.takeState = takeState(dsl, reactReq.take_id);
				reactRes.userState = reactReq.userState;
				return reactRes;
			}
		});
	}

	///////////////
	// TakeState //
	///////////////
	private static TakeReactionJson.TakeState takeState(DSLContext dsl, int take_id) {
		TakeReactionJson.TakeState takeState = new TakeReactionJson.TakeState();
		takeState.viewCount = countForAllUsers(dsl, take_id, Reaction.view);
		takeState.likeCount = countForAllUsers(dsl, take_id, Reaction.like);
		takeState.bookmarkCount = countForAllUsers(dsl, take_id, Reaction.bookmark);
		return takeState;
	}

	private static int countForAllUsers(DSLContext dsl, int takeId, Reaction reaction) {
		return dsl.fetchCount(
				dsl.selectFrom(TAKEREACTION)
						.where(TAKEREACTION.TAKE_ID.eq(takeId)
								.and(TAKEREACTION.KIND.eq(reaction))));
	}

	///////////////
	// UserState //
	///////////////
	private static List<Reaction> reactionsForUser(DSLContext dsl, int takeId, AuthUser user) {
		return dsl.selectFrom(TAKEREACTION)
				.where(TAKEREACTION.TAKE_ID.eq(takeId).and(TAKEREACTION.USER_ID.eq(user.id())))
				.fetch(TAKEREACTION.KIND);
	}

	private static TakeReactionJson.UserState userState(List<Reaction> reactions) {
		TakeReactionJson.UserState userState = new TakeReactionJson.UserState();
		for (Reaction reaction : REACTIONS_NON_VIEW) {
			setReaction(userState, reaction, reactions.contains(reaction));
		}
		return userState;
	}

	private static void setReaction(DSLContext dsl, Request req, int take_id, AuthUser user, Reaction reaction, boolean command) {
		if (command) {
			TakereactionRecord record = dsl.newRecord(TAKEREACTION);
			record.setTakeId(take_id);
			record.setUserId(user.id());
			record.setKind(reaction);
			record.setReactedAt(req.require(Time.class).nowTimestamp());
			record.setReactedIp(req.require(IpGetter.class).ip(req));
			record.store();
		} else {
			dsl.deleteFrom(TAKEREACTION).where(
					TAKEREACTION.TAKE_ID.eq(take_id)
							.and(TAKEREACTION.USER_ID.eq(user.id())
									.and(TAKEREACTION.KIND.eq(reaction))))
					.execute();
		}
	}

	// @formatter:off
	private static final Reaction[] REACTIONS_NON_VIEW = new Reaction[] {
			Reaction.like,
			Reaction.bookmark,
			Reaction.spam,
			Reaction.illegal
	};

	private static void setReaction(TakeReactionJson.UserState state, Reaction reaction, boolean value) {
		switch (reaction) {
		case like:		state.like = value;		break;
		case bookmark:	state.bookmark = value;	break;
		case spam:		state.spam = value;		break;
		case illegal:	state.illegal = value;	break;
		default: throw Unhandled.enumException(reaction);
		}
	}

	private static boolean getReaction(TakeReactionJson.UserState state, Reaction reaction) {
		switch (reaction) {
		case like:		return state.like;
		case bookmark:	return state.bookmark;
		case spam:		return state.spam;
		case illegal:	return state.illegal;
		default: throw Unhandled.enumException(reaction);
		}
	}
	// @formatter:on
}