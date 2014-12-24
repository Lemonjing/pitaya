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

package org.kocakosm.pitaya.util;

import static org.junit.Assert.*;

import org.kocakosm.pitaya.charset.ASCII;

import java.util.Random;

import org.junit.Test;

/**
 * Base16 unit tests.
 *
 * @author Osman KOCAK
 */
public final class Base16Test
{
	@Test
	public void testRFC4648TestVectors()
	{
		assertEquals("", encode(ascii("")));
		assertEquals("66", encode(ascii("f")));
		assertEquals("666F", encode(ascii("fo")));
		assertEquals("666F6F", encode(ascii("foo")));
		assertEquals("666F6F62", encode(ascii("foob")));
		assertEquals("666F6F6261", encode(ascii("fooba")));
		assertEquals("666F6F626172", encode(ascii("foobar")));

		assertArrayEquals(ascii("foobar"), decode("666F6F626172"));
		assertArrayEquals(ascii("fooba"), decode("666F6F6261"));
		assertArrayEquals(ascii("foob"), decode("666F6F62"));
		assertArrayEquals(ascii("foo"), decode("666F6F"));
		assertArrayEquals(ascii("fo"), decode("666F"));
		assertArrayEquals(ascii("f"), decode("66"));
		assertArrayEquals(ascii(""), decode(""));
	}

	@Test
	public void testRandomData()
	{
		Random rnd = new Random();
		for (int i = 0; i < 100; i++) {
			byte[] bytes = new byte[rnd.nextInt(2049)];
			assertArrayEquals(bytes, decode(encode(bytes)));
		}
	}

	@Test
	public void testDecodeWithWhitespaces()
	{
		assertArrayEquals(ascii(""), decode(" \t  \r\n"));
		assertArrayEquals(ascii("hello"), decode("\t68 656C 6C\n6F\r"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidLength()
	{
		decode("E1F0C");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCharacter()
	{
		decode("E1F0HA");
	}

	private String encode(byte... data)
	{
		return BaseEncoding.BASE_16.encode(data);
	}

	private byte[] decode(String base)
	{
		return BaseEncoding.BASE_16.decode(base, 0, base.length());
	}

	private byte[] ascii(String str)
	{
		return ASCII.encode(str);
	}
}
