/*
 * MyTake.org website and tooling.
 * Copyright (C) 2020-2021 MyTake.org, Inc.
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
import com.google.common.xml.XmlEscapers;
import forms.api.RockerRaw;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import java2ts.Routes;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
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
		SearchImage img = new SearchImage(query);
		img.title = "U.S. Presidential Debates | " + query;
		img.desc = "Every time it was said in a televised presidential debate, ever.";
		img.imageAlt = img.desc;
		img.url = "https://mytake.org/search?q=" + UrlEscapers.urlFormParameterEscaper().escape(query);
		return img.result();
	}

	public static SocialEmbed homepage() {
		SearchImage img = new SearchImage("gun control, second amendment");
		img.title = "MyTake.org | True, unbiased, and in context.";
		img.desc = "An open source search engine for unbiased primary sources.";
		img.imageAlt = "A graph over time of how many times \"gun control\" and \"second amendment\" have been said in the presidential debates.";
		img.url = "https://mytake.org";
		return img.result();
	}

	private static class SearchImage {
		private String url;
		private String title;
		private String desc;
		private String imageAlt;
		private final String query;

		public SearchImage(String query) {
			this.query = query;
		}

		public SocialEmbed result() {
			String risonQuery = "'" + UrlEscapers.urlFragmentEscaper().escape(query) + "'";
			String imageUrl = "https://node.mytake.org/static/social-image/~kind:searchResults,factsetHash:E74aoUY,query:" + risonQuery + ".png";
			String imageTwitterUrl = "https://node.mytake.org/static/social-image-twitter/~kind:searchResults,factsetHash:E74aoUY,query:" + risonQuery + ".png";
			return new SocialEmbed(
					"<meta content=\"article\" property=\"og:type\">\n" +
							"<meta property=\"og:title\" content=\"" + XmlEscapers.xmlAttributeEscaper().escape(title) + "\">\n" +
							"<meta property=\"og:description\" content=\"" + XmlEscapers.xmlAttributeEscaper().escape(desc) + "\">\n" +
							"<meta property=\"og:image:alt\" content=\"" + XmlEscapers.xmlAttributeEscaper().escape(imageAlt) + "\">\n" +
							"<meta property=\"og:url\" content=\"" + url + "\">\n" +
							"<meta property=\"og:image\" content=\"" + XmlEscapers.xmlAttributeEscaper().escape(imageUrl) + "\">\n" +
							"<meta property=\"og:image:secure_url\" content=\"" + XmlEscapers.xmlAttributeEscaper().escape(imageUrl) + "\">\n" +
							"<meta property=\"og:image:type\" content=\"image/png\">\n" +
							"<meta property=\"og:image:width\" content=\"1200\">\n" +
							"<meta property=\"og:image:height\" content=\"628\">\n" +
							"<meta name=\"twitter:card\" content=\"summary_large_image\">\n" +
							"<meta name=\"twitter:site\" content=\"@mytakedotorg\">\n" +
							"<meta name=\"twitter:title\" content=\"" + XmlEscapers.xmlAttributeEscaper().escape(title) + "\">\n" +
							"<meta name=\"twitter:description\" content=\"" + XmlEscapers.xmlAttributeEscaper().escape(desc) + "\">\n" +
							"<meta name=\"twitter:image:alt\" content=\"" + XmlEscapers.xmlAttributeEscaper().escape(imageAlt) + "\">\n" +
							"<meta name=\"twitter:image\" content=\"" + XmlEscapers.xmlAttributeEscaper().escape(imageTwitterUrl) + "\">\n");
		}
	}
}
