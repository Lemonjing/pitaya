/*----------------------------------------------------------------------------*
 * This file is part of Pitaya.                                               *
 * Copyright (C) 2012-2016 Osman KOCAK <kocakosm@gmail.com>                   *
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

package org.kocakosm.pitaya.collection;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

/**
 * {@link ArrayBag}'s unit tests.
 *
 * @author Osman KOCAK
 */
public final class ArrayBagTest
{
	@Test
	public void testIterableContructor()
	{
		Iterable<String> iterable = Arrays.asList("Hello", "World");
		Bag<String> bag = new ArrayBag<String>(iterable);
		assertEquals(2, bag.size());
		assertTrue(bag.contains("Hello"));
		assertTrue(bag.contains("World"));
	}

	@Test
	public void testIteratorContructor()
	{
		Collection<String> collection = Arrays.asList("Hello", "World");
		Bag<String> bag = new ArrayBag<String>(collection.iterator());
		assertEquals(2, bag.size());
		assertTrue(bag.contains("Hello"));
		assertTrue(bag.contains("World"));
	}

	@Test
	public void testArrayContructor()
	{
		Bag<String> bag = new ArrayBag<String>("Hello", "World");
		assertEquals(2, bag.size());
		assertTrue(bag.contains("Hello"));
		assertTrue(bag.contains("World"));
	}

	@Test
	public void testAdd()
	{
		Bag<String> bag = new ArrayBag<String>();
		bag.add("Hello");
		assertTrue(bag.contains("Hello"));
	}

	@Test
	public void testAddAll()
	{
		Bag<String> bag = new ArrayBag<String>();
		assertTrue(bag.addAll(Arrays.asList("Hello", "World")));
		assertTrue(bag.contains("Hello"));
		assertTrue(bag.contains("World"));
	}

	@Test
	public void testClear()
	{
		Bag<String> bag = new ArrayBag<String>("Hello", "World");
		assertFalse(bag.isEmpty());
		bag.clear();
		assertTrue(bag.isEmpty());
	}

	@Test
	public void testContains()
	{
		Bag<String> bag = new ArrayBag<String>("Hello", "World");
		assertTrue(bag.contains("Hello"));
		assertTrue(bag.contains("World"));
		assertFalse(bag.contains("Bye"));
		assertFalse(bag.contains(null));

		bag = new ArrayBag<String>("271", null);
		assertTrue(bag.contains("271"));
		assertFalse(bag.contains("Hello"));
		assertTrue(bag.contains(null));
	}

	@Test
	public void testContainsAll()
	{
		Bag<String> bag = new ArrayBag<String>("Hello", "World");
		assertTrue(bag.containsAll(bag));
		assertTrue(bag.containsAll(Arrays.asList("Hello")));
	}

	@Test
	public void testCount()
	{
		Bag<String> bag = new ArrayBag<String>();
		assertEquals(0, bag.count("Hello"));
		bag.add("World");
		assertEquals(0, bag.count("Hello"));
		bag.add("Hello");
		assertEquals(1, bag.count("Hello"));
		bag.add("World");
		assertEquals(1, bag.count("Hello"));
		bag.add("Hello");
		assertEquals(2, bag.count("Hello"));
	}

	@Test
	public void testIsEmpty()
	{
		assertTrue(new ArrayBag<Integer>().isEmpty());
		assertFalse(new ArrayBag<String>("Hello").isEmpty());
	}

	@Test
	public void testIterator()
	{
		assertFalse(new ArrayBag<Integer>().iterator().hasNext());
		List<Long> in = Arrays.asList(1L, 2L, 1L, 2L, 3L);
		Bag<Long> bag = new ArrayBag<Long>(in);
		assertEquals(in, Iterators.toList(bag.iterator()));
	}

	@Test
	public void testRemove()
	{
		Bag<Long> bag = new ArrayBag<Long>(1L, 2L, 1L);
		assertFalse(bag.remove(5L));
		assertTrue(bag.remove(1L));
		assertTrue(bag.contains(1L));
		assertTrue(bag.remove(1L));
		assertFalse(bag.contains(1L));
		assertTrue(bag.remove(2L));
		assertFalse(bag.contains(2L));
	}

	@Test
	public void testRemoveAll()
	{
		assertFalse(new ArrayBag<Long>(1L, 5L, 1L).removeAll(Arrays.asList(2L)));
		Bag<Long> bag = new ArrayBag<Long>(1L, 2L, 1L, 3L);
		assertTrue(bag.removeAll(Arrays.asList(1L, 2L, 5L)));
		assertFalse(bag.contains(1L));
		assertFalse(bag.contains(2L));
		assertTrue(bag.contains(3L));
	}

	@Test
	public void testRetainAll()
	{
		assertFalse(new ArrayBag<Long>(2L, 2L).retainAll(Arrays.asList(2L)));
		Bag<Long> bag = new ArrayBag<Long>(1L, 2L, 1L, 3L);
		assertTrue(bag.retainAll(Arrays.asList(2L)));
		assertTrue(bag.contains(2L));
		assertFalse(bag.contains(1L));
		assertFalse(bag.contains(3L));
	}

	@Test
	public void testSize()
	{
		Bag<String> bag = new ArrayBag<String>();
		assertEquals(0, bag.size());
		bag.add("Hello");
		assertEquals(1, bag.size());
		bag.add("World");
		assertEquals(2, bag.size());
		bag.add("Bye");
		assertEquals(3, bag.size());
		bag.add("World");
		assertEquals(4, bag.size());
	}

	@Test
	public void testToArray()
	{
		assertEquals(0, new ArrayBag<String>().toArray().length);
		String[] in = new String[]{"Hello", "World"};
		String[] out = new ArrayBag<String>(in).toArray(new String[0]);
		assertArrayEquals(in, out);
	}

	@Test
	public void testEqualsAndHashCode()
	{
		Bag<Integer> bag1 = new ArrayBag<Integer>(1, 2, 3, 1, 2, 3);
		Bag<Integer> bag2 = new ArrayBag<Integer>(3, 1, 2, 3, 1, 2);
		Bag<Integer> bag3 = new ArrayBag<Integer>(2, 3, 1, 2, 3, 1);

		assertTrue(bag1.equals(bag1));
		assertTrue(bag1.equals(bag2));
		assertTrue(bag2.equals(bag1));
		assertTrue(bag2.equals(bag3));
		assertTrue(bag3.equals(bag2));
		assertTrue(bag1.equals(bag3));
		assertTrue(bag3.equals(bag1));
		assertTrue(bag1.hashCode() == bag2.hashCode());
		assertTrue(bag2.hashCode() == bag3.hashCode());

		bag2.add(3);
		assertFalse(bag1.equals(bag2));
		assertFalse(bag2.equals(bag1));

		bag2 = null;
		assertFalse(bag1.equals(bag2));
	}
}
