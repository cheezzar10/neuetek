package com.imes.rnd.jpa.dellstore;

import java.util.Spliterator;
import java.util.function.Consumer;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

public class SQLQueryResultSpliterator implements Spliterator<Object[]> {
private static final ScrollMode SCROLL_MODE = ScrollMode.SCROLL_INSENSITIVE; 
	
	private final ScrollableResults cursor;
	private final int column;

	public SQLQueryResultSpliterator(SQLQuery query) {
		this(query, -1, SCROLL_MODE);
	}
	
	public SQLQueryResultSpliterator(SQLQuery query, int column) {
		this(query, column, SCROLL_MODE);
	}
	
	private SQLQueryResultSpliterator(SQLQuery query, int column, ScrollMode scrollMode) {
		this.cursor = query.scroll(scrollMode);
		this.column = column;
	}

	@Override
	public boolean tryAdvance(Consumer<? super Object[]> action) {
		if (cursor.next()) {
			if (column == -1) {
				action.accept(cursor.get());
			} else {
				action.accept(new Object[] { cursor.get(column) });
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Spliterator<Object[]> trySplit() {
		// not supported
		return null;
	}

	@Override
	public long estimateSize() {
		// unknown
		return Long.MAX_VALUE;
	}

	@Override
	public int characteristics() {
		return Spliterator.ORDERED | Spliterator.IMMUTABLE;
	}
}
