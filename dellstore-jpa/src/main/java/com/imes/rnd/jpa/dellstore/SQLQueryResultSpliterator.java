package com.imes.rnd.jpa.dellstore;

import java.util.Spliterator;
import java.util.function.Consumer;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

public class SQLQueryResultSpliterator implements Spliterator<Object[]> {
private static final ScrollMode DEFAULT_SCROLL_MODE = ScrollMode.SCROLL_INSENSITIVE; 
	
	private final ScrollableResults cursor;
	private final int column;
	private final int scrollingPause;

	public SQLQueryResultSpliterator(SQLQuery query) {
		this(query, -1, DEFAULT_SCROLL_MODE, -1);
	}
	
	public SQLQueryResultSpliterator(SQLQuery query, int column) {
		this(query, column, DEFAULT_SCROLL_MODE, -1);
	}
	
	private SQLQueryResultSpliterator(SQLQuery query, int column, ScrollMode scrollMode, int pause) {
		this.cursor = query.scroll(scrollMode);
		this.column = column;
		scrollingPause = pause;
	}

	@Override
	public boolean tryAdvance(Consumer<? super Object[]> action) {
		if (cursor.next()) {
			if (column == -1) {
				action.accept(cursor.get());
			} else {
				action.accept(new Object[] { cursor.get(column) });
			}
			
			boolean interrupted = pause();
			if (interrupted) {
				return false;
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	private boolean pause() {
		if (scrollingPause > 0) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException interuptedEx) {
				Thread.currentThread().interrupt();
				return true;
			}
		}
		
		return false;
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
