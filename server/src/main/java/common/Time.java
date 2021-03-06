/*
 * MyTake.org website and tooling.
 * Copyright (C) 2017-2020 MyTake.org, Inc.
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

/** Abstracts access to time, so we can control it for testing. */
public interface Time {
	/** DateTime in UTC. */
	LocalDateTime now();

	/** Jan 01 1970 */
	public static DateTimeFormatter formatCompact() {
		return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
	}

	public static LocalDateTime parseIso(String str) {
		return LocalDateTime.from(Instant.parse(str));
	}

	public static String toIso(LocalDateTime timestamp) {
		return OffsetDateTime.of(timestamp, ZoneOffset.UTC).toString();
	}

	public static LocalDateTime parseGMT(String str) {
		return LocalDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(str));
	}

	public static String toGMT(LocalDateTime timestamp) {
		return DateTimeFormatter.RFC_1123_DATE_TIME.format(timestamp.atOffset(ZoneOffset.UTC));
	}

	public static Date toJud(LocalDateTime dateTime) {
		return Date.from(dateTime.atZone(ZoneOffset.UTC).toInstant());
	}

	public static LocalDateTime fromJud(Date date) {
		return date.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
	}
}
