/*
 * MyTake.org
 *
 *  Copyright 2017 by its authors.
 *  Some rights reserved. See LICENSE, https://github.com/mytake/mytake/graphs/contributors
 */
package controllers;

import common.JoobyDevRule;
import common.JsonPost;
import io.restassured.RestAssured;
import java2ts.Routes;
import java2ts.TakeReactionJson;
import java2ts.TakeReactionJson.UserState;
import javax.annotation.Nullable;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TakeReactionTest {
	@ClassRule
	public static JoobyDevRule dev = JoobyDevRule.initialData();

	private static final int TEST_TAKE_ID = 1;

	private static TakeReactionJson.ViewRes viewForUser(@Nullable String username) {
		TakeReactionJson.ViewReq req = new TakeReactionJson.ViewReq();
		req.take_id = TEST_TAKE_ID;
		return JsonPost.post(username == null ? RestAssured.given() : dev.givenUser(username),
				req, Routes.API_TAKE_VIEW, TakeReactionJson.ViewRes.class);
	}

	@Test
	public void _01_viewNotLoggedIn() {
		TakeReactionJson.ViewRes res = viewForUser(null);
		Assertions.assertThat(res.userState).isNull();
		Assertions.assertThat(res.takeState.viewCount).isEqualTo(0);
		Assertions.assertThat(res.takeState.likeCount).isEqualTo(0);
	}

	@Test
	public void _02_viewLoggedIn() {
		TakeReactionJson.ViewRes res = viewForUser("samples");
		Assertions.assertThat(res.userState).isNotNull();
		Assertions.assertThat(res.userState.like).isFalse();
		Assertions.assertThat(res.userState.bookmark).isFalse();
		Assertions.assertThat(res.userState.spam).isFalse();
		Assertions.assertThat(res.userState.harassment).isFalse();
		Assertions.assertThat(res.userState.rulesviolation).isFalse();
		Assertions.assertThat(res.takeState.viewCount).isEqualTo(1);
		Assertions.assertThat(res.takeState.likeCount).isEqualTo(0);
	}

	private static TakeReactionJson.ReactRes reactForUser(String username, UserState userState) {
		TakeReactionJson.ReactReq req = new TakeReactionJson.ReactReq();
		req.take_id = TEST_TAKE_ID;
		req.userState = userState;
		return JsonPost.post(dev.givenUser(username), req, Routes.API_TAKE_REACT, TakeReactionJson.ReactRes.class);
	}

	@Test
	public void _03_like() {
		UserState userState = new UserState();
		userState.like = true;
		TakeReactionJson.ReactRes res = reactForUser("samples", userState);
		Assertions.assertThat(res.userState).isNotNull();
		Assertions.assertThat(res.userState.like).isTrue();
		Assertions.assertThat(res.userState.bookmark).isFalse();
		Assertions.assertThat(res.userState.spam).isFalse();
		Assertions.assertThat(res.userState.harassment).isFalse();
		Assertions.assertThat(res.userState.rulesviolation).isFalse();
		Assertions.assertThat(res.takeState.viewCount).isEqualTo(1);
		Assertions.assertThat(res.takeState.likeCount).isEqualTo(1);
	}

	@Test
	public void _04_unlike_and_bookmark() {
		UserState userState = new UserState();
		userState.like = false;
		userState.bookmark = true;
		TakeReactionJson.ReactRes res = reactForUser("samples", userState);
		Assertions.assertThat(res.userState).isNotNull();
		Assertions.assertThat(res.userState.like).isFalse();
		Assertions.assertThat(res.userState.bookmark).isTrue();
		Assertions.assertThat(res.userState.spam).isFalse();
		Assertions.assertThat(res.userState.harassment).isFalse();
		Assertions.assertThat(res.userState.rulesviolation).isFalse();
		Assertions.assertThat(res.takeState.viewCount).isEqualTo(1);
		Assertions.assertThat(res.takeState.likeCount).isEqualTo(0);
	}

	@Test
	public void _05_other_view() {
		TakeReactionJson.ViewRes res = viewForUser("other");
		Assertions.assertThat(res.userState).isNotNull();
		Assertions.assertThat(res.userState.like).isFalse();
		Assertions.assertThat(res.userState.bookmark).isFalse();
		Assertions.assertThat(res.userState.spam).isFalse();
		Assertions.assertThat(res.userState.harassment).isFalse();
		Assertions.assertThat(res.userState.rulesviolation).isFalse();
		Assertions.assertThat(res.takeState.viewCount).isEqualTo(2);
		Assertions.assertThat(res.takeState.likeCount).isEqualTo(0);
	}
}
