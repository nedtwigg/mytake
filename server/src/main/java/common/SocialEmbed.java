/*
 * MyTake.org website and tooling.
 * Copyright (C) 2020 MyTake.org, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can contact us at team@mytake.org
 */
package common;

import com.diffplug.common.base.Unhandled;
import com.google.common.net.UrlEscapers;
import forms.api.RockerRaw;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import java2ts.Routes;
import javax.annotation.Nullable;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jooby.Env;
import org.jooby.Jooby;
import views.SocialEmbed.socialImage;

public class SocialEmbed {
	private static final String DEV_URL = "/devSocialEmbed";

	private static final int MAX_WAIT_MS = 500;
	private static final int NODE_DEV_PORT = 4000;
	private static final boolean isHerokuProd = "true".equals(System.getenv("HEROKU_NAKED_PROD"));

	public static SocialEmbed get(org.jooby.Request req, String socialRison) throws IOException {
		return req.require(GetHeader.class).get(req, socialRison);
	}

	public static SocialEmbed todo() {
		return null;
	}

	public static SocialEmbed todo(String input) {
		return null;
	}

	private String metaTags;

	private SocialEmbed(String metaTags) {
		this.metaTags = metaTags;
	}

	public RockerRaw header() {
		return RockerRaw.raw(metaTags);
	}

	static class GetHeader {
		public SocialEmbed get(org.jooby.Request req, String socialRison) {
			System.err.println("social embed for " + req.rawPath());
			System.err.println("  http://localhost:3000" + DEV_URL + "#" + socialRison);
			return new SocialEmbed("<!-- socialRison: " + socialRison + " -->");
		}
	}

	static class GetHeaderProd extends GetHeader {
		private final String httpDomain;
		private final HttpUrl httpDomainOk;
		private final OkHttpClient client;

		private GetHeaderProd(Env env, @Nullable String httpDomain) {
			this.httpDomain = httpDomain;
			this.httpDomainOk = HttpUrl.get(httpDomain);

			int numConnections = env.config().getInt("runtime.processors-x2");
			int minutesIdleBeforeClosed = 1;
			client = new OkHttpClient.Builder()
					.callTimeout(MAX_WAIT_MS, TimeUnit.MILLISECONDS)
					.retryOnConnectionFailure(true)
					.connectionPool(new ConnectionPool(numConnections, minutesIdleBeforeClosed, TimeUnit.MINUTES))
					.build();

			env.onStop(client.connectionPool()::evictAll);
		}

		@Override
		public SocialEmbed get(org.jooby.Request req, String socialRison) {
			String body;
			{
				HttpUrl url = httpDomainOk.newBuilder().encodedPath(Routes.PATH_NODE_SOCIAL_HEADER + socialRison).build();
				try (Response response = client.newCall(new okhttp3.Request.Builder().url(url).build()).execute()) {
					body = response.body().string();
				} catch (IOException e) {
					System.err.println("-----------------------");
					System.err.println(url.toString());
					e.printStackTrace();
					// this ought to hook into https://github.com/mytakedotorg/mtdo/issues/328
					body = "<!-- temporary header failure, don't cache -->";
				}
			}
			if (!httpDomain.equals(HTTPS_NODE)) {
				// node.mytake.org always returns node.mytake.org image URLs, even when running on
				// dev machines or the staging instance on Heroku, so we have to adjust for that here.
				body = body.replace(HTTPS_NODE, httpDomain);
			}
			return new SocialEmbed(body);
		}
	}

	public static void init(Jooby jooby) {
		jooby.use((env, conf, binder) -> {
			if (!isHerokuProd) {
				// everywhere besides prod, we want to show the social preview here
				env.router().get(DEV_URL, req -> socialImage.template());
			}
			// bind the GetHeader instance for all templates
			binder.bind(GetHeader.class).toInstance(getInstance(env));
		});
	}

	private static GetHeader getInstance(Env env) throws IOException {
		String base;
		if (env.name().equals("dev")) {
			try (ServerSocket socket = new ServerSocket(NODE_DEV_PORT)) {
				// the node server is not running, so we will use the dev instance
				return new GetHeader();
			} catch (BindException e) {
				// the node server is running, so we'll work with it
				base = HTTP_LOCAL_DEV;
			}
		} else if (isHerokuProd) {
			base = HTTPS_NODE;
		} else if (env.name().equals("heroku")) {
			base = "https://mtdo-node-staging.herokuapp.com";
		} else {
			throw Unhandled.stringException(env.name());
		}
		return new GetHeaderProd(env, base);
	}

	private static final String HTTPS_NODE = "https://node.mytake.org";
	private static final String HTTP_LOCAL_DEV = "http://localhost:" + NODE_DEV_PORT;

	public static SocialEmbed search(String query) {
		// copied from SocialHeader.spec.tsx
		return new SocialEmbed(
				"<meta content=\"" + query + "\" in presidential debates\" property=\"og:title\">\n" +
						"<meta content=\"Every single time that \"" + query + "\" was said in a presidential debate, ever.\" property=\"og:description\">\n" +
						"<meta content=\"article\" property=\"og:type\">\n" +
						"<meta content=\"https://mytake.org/search?q=" + UrlEscapers.urlFormParameterEscaper().escape(query) + "\" property=\"og:url\">\n" +
						"<meta content=\"https://node.mytake.org/static/social-image/~kind:searchResults,query:'" + query + "'.png\" property=\"og:image\">\n" +
						"<meta content=\"https://node.mytake.org/static/social-image/~kind:searchResults,query:'" + query + "'.png\" property=\"og:image:secure_url\">\n" +
						"<meta content=\"image/png\" property=\"og:image:type\">\n" +
						"<meta content=\"1200\" property=\"og:image:width\">\n" +
						"<meta content=\"628\" property=\"og:image:height\">\n" +
						"<meta content=\"A bar graph of how many times \"" + query + "\" was said in a presidential debate.\" property=\"og:image:alt\">\n" +
						"<meta content=\"summary_large_image\" name=\"twitter:card\">\n" +
						"<meta content=\"@mytakedotorg\" name=\"twitter:site\">\n" +
						"<meta content=\"" + query + "\" in presidential debates\" name=\"twitter:title\">\n" +
						"<meta content=\"Every single time that \"" + query + "\" was said in a presidential debate, ever.\" name=\"twitter:description\">\n" +
						"<meta content=\"https://node.mytake.org/static/social-image-twitter/~kind:searchResults,query:'" + query + "'.png\" name=\"twitter:image\">\n" +
						"<meta content=\"A bar graph of how many times \"" + query + "\" was said in a presidential debate.\" name=\"twitter:image:alt\">\n");
	}
}
