/*
 * MyTake.org
 *
 *  Copyright 2017 by its authors.
 *  Some rights reserved. See LICENSE, https://github.com/mytakedotorg/mytakedotorg/graphs/contributors
 */
package common;

import static db.Tables.ACCOUNT;
import static db.Tables.MODERATOR;
import static db.Tables.TAKEDRAFT;
import static db.Tables.TAKEPUBLISHED;
import static db.Tables.TAKEREVISION;

import com.google.common.io.Resources;
import com.google.inject.Binder;
import com.typesafe.config.Config;
import db.Tables;
import db.tables.records.AccountRecord;
import db.tables.records.TakedraftRecord;
import db.tables.records.TakepublishedRecord;
import db.tables.records.TakerevisionRecord;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooq.DSLContext;
import org.jooq.JSONB;

/** This is for setting the initial data for the case that the database is empty. */
public class InitialData {
	/** If the database is empty, initializes with some data. */
	public static class Module implements Jooby.Module {
		@Override
		public void configure(Env env, Config conf, Binder binder) throws Throwable {
			env.onStart(registry -> {
				try (DSLContext dsl = registry.require(DSLContext.class)) {
					int numAccounts = dsl.fetchCount(Tables.ACCOUNT);
					if (numAccounts == 0) {
						init(dsl, registry.require(Time.class));
					}
				}
			});
		}
	}

	public static void init(DSLContext dsl, Time time) throws Exception {
		int sampleUser = usernameEmail(dsl, time, "samples", "samples@email.com");
		take(dsl, time, sampleUser, "Why it's so hard to have peace", "/presidential-debate-clinton-trump-2-of-3_304.000-321.000.png");
		take(dsl, time, sampleUser, "Does a law mean what it says, or what it meant?", "/bill-of-rights_283-439_283-439.png");
		take(dsl, time, sampleUser, "Don't worry, we'll protect the Constitution for you!", "/united-states-constitution_17730-18357_17730-18357.png");

		draft(dsl, time, sampleUser, "In my opinion", b -> {});
		draft(dsl, time, sampleUser, "In other words", b -> b.p("What a world"));

		int otherUser = usernameEmail(dsl, time, "other", "other@email.com");
		take(dsl, time, otherUser, "I am a strawman", b -> {});
		draft(dsl, time, otherUser, "To make sure users don't leak", b -> {});

		usernameEmail(dsl, time, "empty", "empty@email.com");

		dsl.insertInto(MODERATOR).columns(MODERATOR.ID)
				.values(usernameEmail(dsl, time, "mod1", "mod1@email.com"))
				.values(usernameEmail(dsl, time, "mod2", "mod2@email.com"))
				.execute();
	}

	static TakerevisionRecord draft(DSLContext dsl, Time time, int user, String title, TakeBuilder builder) {
		TakerevisionRecord rev = dsl.newRecord(TAKEREVISION);
		rev.setCreatedAt(time.nowTimestamp());
		rev.setCreatedIp(IP);
		rev.setTitle(title);
		rev.setBlocks(builder.buildJson());
		rev.insert();

		TakedraftRecord draft = dsl.newRecord(TAKEDRAFT);
		draft.setUserId(user);
		draft.setLastRevision(rev.getId());
		draft.insert();

		return rev;
	}

	static TakerevisionRecord draft(DSLContext dsl, Time time, int user, String title, Consumer<TakeBuilder> builder) {
		return draft(dsl, time, user, title, TakeBuilder.builder(builder));
	}

	static int usernameEmail(DSLContext dsl, Time time, String username, String email) {
		AccountRecord record = dsl.newRecord(ACCOUNT);
		record.setUsername(username);
		record.setEmail(email);
		record.setCreatedAt(time.nowTimestamp());
		record.setCreatedIp(IP);
		record.setUpdatedAt(time.nowTimestamp());
		record.setUpdatedIp(IP);
		record.setLastSeenAt(time.nowTimestamp());
		record.setLastSeenIp(IP);
		record.setLastEmailedAt(time.nowTimestamp());
		record.insert();
		return record.getId();
	}

	static TakepublishedRecord take(DSLContext dsl, Time time, int user, String title, String factTitleSlug) throws IOException {
		TakepublishedRecord record = takeInternal(dsl, time, user, title);
		JSONB jsonData = JSONB.valueOf(Resources.toString(Resources.getResource("initialdata/" + record.getTitleSlug() + ".json"), StandardCharsets.UTF_8));
		record.setBlocks(jsonData);
		record.setImageUrl(factTitleSlug);
		record.insert();
		return record;
	}

	static TakepublishedRecord take(DSLContext dsl, Time time, int user, String title, Consumer<TakeBuilder> b) throws IOException {
		return take(dsl, time, user, title, TakeBuilder.builder(b));
	}

	static TakepublishedRecord take(DSLContext dsl, Time time, int user, String title, TakeBuilder b) throws IOException {
		TakepublishedRecord record = takeInternal(dsl, time, user, title);
		record.setBlocks(b.buildJson());
		record.insert();
		return record;
	}

	private static TakepublishedRecord takeInternal(DSLContext dsl, Time time, int user, String title) {
		TakepublishedRecord record = dsl.newRecord(TAKEPUBLISHED);
		record.setUserId(user);
		record.setTitle(title);
		String slugified = Text.slugify(title);
		record.setTitleSlug(slugified);
		record.setPublishedAt(time.nowTimestamp());
		record.setPublishedIp(IP);
		return record;
	}

	private static final String IP = "127.0.0.1";
}
