/*
 * MyTake.org
 *
 *  Copyright 2017 by its authors.
 *  Some rights reserved. See LICENSE, https://github.com/mytake/mytake/graphs/contributors
 */
package common;

import com.fizzed.rocker.RockerModel;
import com.google.common.base.Preconditions;
import forms.api.RockerRaw;
import org.jooby.rocker.RequestRockerTemplate;

public abstract class CustomRockerTemplate extends RequestRockerTemplate {
	public CustomRockerTemplate(RockerModel model) {
		super(model);
	}

	/** Returns link tags for the css files with the given fileset. */
	public RockerRaw style(String fileset) {
		String linkTags = (String) locals.get(fileset + "_styles");
		Preconditions.checkNotNull(linkTags, "No such styles %s, available: %s", fileset, locals.keySet());
		return new RockerRaw().appendRaw(linkTags);
	}

	/** Returns script tags for the js files with the given fileset. */
	public RockerRaw script(String fileset) {
		String scriptTags = (String) locals.get(fileset + "_scripts");
		Preconditions.checkNotNull(scriptTags, "No such scripts %s, available: %s", fileset, locals.keySet());
		return new RockerRaw().appendRaw(scriptTags);
	}
}
