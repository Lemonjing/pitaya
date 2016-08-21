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

package org.kocakosm.pitaya.io;

import org.kocakosm.pitaya.util.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An {@code InputStream} which reads sequentially from multiple sources. Mark
 * and reset are not supported. Not thread safe.
 *
 * @author Osman KOCAK
 */
final class ConcatInputStream extends InputStream
{
	private int index;
	private final List<InputStream> streams;

	/**
	 * Creates a new {@code ConcatInputStream}.
	 *
	 * @param streams the streams to concatenate.
	 *
	 * @throws NullPointerException if {@code streams} is {@code null} or
	 *	if it contains a {@code null} reference.
	 * @throws IllegalArgumentException if {@code streams} is empty.
	 */
	ConcatInputStream(InputStream... streams)
	{
		this(Arrays.asList(streams));
	}

	/**
	 * Creates a new {@code ConcatInputStream}.
	 *
	 * @param streams the streams to concatenate.
	 *
	 * @throws NullPointerException if {@code streams} is {@code null} or
	 *	if it contains a {@code null} reference.
	 * @throws IllegalArgumentException if {@code streams} is empty.
	 */
	ConcatInputStream(Iterable<? extends InputStream> streams)
	{
		this.streams = new ArrayList<InputStream>();
		for (InputStream stream : streams) {
			this.streams.add(Parameters.checkNotNull(stream));
		}
		Parameters.checkCondition(!this.streams.isEmpty());
	}

	@Override
	public int available() throws IOException
	{
		return finished() ? 0 : current().available();
	}

	@Override
	public void close()
	{
		for (InputStream stream : streams) {
			IO.close(stream);
		}
	}

	@Override
	public int read() throws IOException
	{
		int b = finished() ? -1 : current().read();
		return b != -1 ? b : next() ? read() : -1;
	}

	@Override
	public int read(byte[] b) throws IOException
	{
		int n = finished() ? -1 : current().read(b);
		return n != -1 ? n : next() ? read(b) : -1;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		int n = finished() ? -1 : current().read(b, off, len);
		return n != -1 ? n : next() ? read(b, off, len) : -1;
	}

	@Override
	public long skip(long n) throws IOException
	{
		return finished() ? 0L : current().skip(n);
	}

	private InputStream current()
	{
		return streams.get(index);
	}

	private boolean finished()
	{
		return index >= streams.size();
	}

	private boolean next()
	{
		index++;
		return !finished();
	}
}
