package org.mytake.gradle.hash;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.jgit.util.sha1.SHA1;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;

public class HashTask extends DefaultTask {
	private List<File> folders = new ArrayList<>();
	private List<String> strings = new ArrayList<>();
	private File destination;

	public void addFolder(Object folderObj) {
		File folder = getProject().file(folderObj);
		getInputs().dir(folder).withPathSensitivity(PathSensitivity.RELATIVE);
		folders.add(folder);
	}

	public void addStrings(String... strings) {
		addStrings(Arrays.asList(strings));
	}

	public void addStrings(Iterable<String> strings) {
		for (String string : strings) {
			this.strings.add(string);
		}
	}

	public void destination(Object destination) {
		this.destination = getProject().file(destination);
	}

	@Input
	public List<String> getStrings() {
		return strings;
	}

	@OutputFile
	public File getDestination() {
		return destination;
	}

	@TaskAction
	public void action() throws IOException {
		SHA1 buffer = SHA1.newInstance();
		for (File folder : folders) {
			buffer.update(hashTree(folder.toPath()));
		}
		for (String string : strings) {
			buffer.update(string.getBytes(StandardCharsets.UTF_8));
		}
		Files.createDirectories(destination.toPath().getParent());
		String content = "{\"hash\":\"" + buffer.toObjectId().name() + "\"}"; 
		Files.write(destination.toPath(), content.getBytes(StandardCharsets.UTF_8));
	}

	/** Hashes the given folder. */
	private static byte[] hashTree(Path rootFolder) throws IOException {
		TreeMap<String, byte[]> tree = new TreeMap<>();
		Files.walkFileTree(rootFolder, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String path = rootFolder.relativize(file).toString();
				byte[] content = Files.readAllBytes(file);
				tree.put(path, hashBlob(content));
				return FileVisitResult.CONTINUE;
			}
		});
		SHA1 buffer = SHA1.newInstance();
		tree.forEach((path, sha) -> {
			buffer.update(path.getBytes(StandardCharsets.UTF_8));
			buffer.update((byte) 0);
			buffer.update(sha);
		});
		return buffer.digest();
	}

	private static byte[] hashBlob(byte[] content) {
		SHA1 sha = SHA1.newInstance();
		// git blob header
		sha.update("blob ".getBytes(StandardCharsets.UTF_8));
		sha.update(Integer.toString(content.length).getBytes(StandardCharsets.UTF_8));
		sha.update((byte) 0);
		// then the content
		sha.update(content);
		return sha.digest();
	}
}