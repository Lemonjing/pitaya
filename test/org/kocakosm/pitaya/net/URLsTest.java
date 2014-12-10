/*----------------------------------------------------------------------------*
 * This file is part of Pitaya.                                               *
 * Copyright (C) 2012-2014 Osman KOCAK <kocakosm@gmail.com>                   *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify it    *
 * under the terms of the GNU Lesser General Public License as published by   *
 * the Free Software Foundation, either version 3 of the License, or (at your *
 * option) any later version.                                                 *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY *
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public     *
 * License for more details.                                                  *
 * You should have received a copy of the GNU Lesser General Public License   *
 * along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *----------------------------------------------------------------------------*/

package org.kocakosm.pitaya.net;

import static org.junit.Assert.*;

import org.kocakosm.pitaya.io.Files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * {@link URLs}' unit tests.
 *
 * @author Osman KOCAK
 */
public final class URLsTest
{
	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	@Test
	public void testCreate() throws Exception
	{
		String url = "http://localhost:8080/test?hello=world";
		assertEquals(toURL(url), URLs.create(url));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateError()
	{
		URLs.create("/test");
	}

	@Test
	public void testRelativize() throws Exception
	{
		URL base = toURL("http://localhost/test");
		URL full = toURL("http://localhost/test/hello/world");
		String relative = "hello/world";
		assertEquals(relative, URLs.relativize(base, full));
	}

	@Test
	public void testResolve() throws Exception
	{
		URL base = toURL("http://localhost/");
		String path = "/hello/world";
		URL resolved = toURL("http://localhost/hello/world");
		assertEquals(resolved, URLs.resolve(base, path));
	}

	@Test
	public void testReadAsByteArray() throws Exception
	{
		byte[] data = "Hello World".getBytes("ASCII");
		File f = tmp.newFile();
		Files.write(f, data);
		assertArrayEquals(data, URLs.read(f.toURI().toURL()));
	}

	@Test
	public void testReadAsString() throws Exception
	{
		String data = "Hello World";
		Charset ascii = Charset.forName("ASCII");
		File f = tmp.newFile();
		Files.write(f, data.getBytes(ascii));
		assertEquals(data, URLs.read(f.toURI().toURL(), ascii));
	}

	@Test
	public void testReadLines() throws Exception
	{
		List<String> lines = Arrays.asList("Hello", "World", "!!!!!");
		StringWriter out = new StringWriter();
		BufferedWriter writer = new BufferedWriter(out);
		writer.write(lines.get(0));	writer.newLine();
		writer.write(lines.get(1));	writer.newLine();
		writer.write(lines.get(2));	writer.flush();
		Charset ascii = Charset.forName("ASCII");
		File f = tmp.newFile();
		Files.write(f, out.toString().getBytes(ascii));
		assertEquals(lines, URLs.readLines(f.toURI().toURL(), ascii));
	}

	private URL toURL(String url) throws Exception
	{
		return new URL(url);
	}
}
