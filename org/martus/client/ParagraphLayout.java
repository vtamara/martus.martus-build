/* $Id: ParagraphLayout.java,v 1.3 2002/04/18 20:42:55 charles Exp $ */
/*
	ParagraphLayout was written by Jerry Huxtable (jerry@jhlabs.com)

	The home page for this package (as of June 6 2001) was:
		http://www.jhlabs.com/java/layout/

	The copyright, according to that web page, is:
		"You can use these in your own code freely, though I'd appreciate
		an email message if you use them or if you find any bugs."
*/
package org.martus.client;


import java.awt.*;

public class ParagraphLayout extends ConstraintLayout {

	public final static Integer NEW_PARAGRAPH = new Integer(0x01);
	public final static Integer NEW_PARAGRAPH_TOP = new Integer(0x02);
	public final static Integer NEW_LINE = new Integer(0x03);

	protected int hGapMajor, vGapMajor;
	protected int hGapMinor, vGapMinor;
	protected int rows;
	protected int colWidth1;
	protected int colWidth2;
	protected int requestedFirstColumnWidth;
	protected boolean isFirstFieldOutdented;

	public ParagraphLayout() {
		this(10, 10, 12, 11, 4, 4);
	}

	public ParagraphLayout(int hMargin, int vMargin, int hGapMajor, int vGapMajor, int hGapMinor, int vGapMinor) {
		this.hMargin = hMargin;
		this.vMargin = vMargin;
		this.hGapMajor = hGapMajor;
		this.vGapMajor = vGapMajor;
		this.hGapMinor = hGapMinor;
		this.vGapMinor = vGapMinor;
	}

	public void outdentFirstField()	{
		isFirstFieldOutdented = true;
	}

	public void setFirstColumnWidth(int firstColumnWidth)	{
		requestedFirstColumnWidth = firstColumnWidth;
	}

	public int getFirstColumnMaxWidth(Container target) {
		int maxWidth = 0;
		int count = target.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component c = target.getComponent(i);
			if (includeComponent(c)) {
				Integer n = (Integer)getConstraint(c);
				if (i == 0 || n == NEW_PARAGRAPH || n == NEW_PARAGRAPH_TOP) {
					Dimension d = getComponentSize(c, PREFERRED);
					maxWidth = Math.max(maxWidth, d.width);
				}
			}
		}

		return maxWidth;
	}

	public void measureLayout(Container target, Dimension dimension, int type)  {
		int count = target.getComponentCount();
		if (count > 0) {
			Insets insets = target.getInsets();
			Dimension size = target.getSize();
			int x = 0;
			int y = 0;
			int rowHeight = 0;
			int colWidth = 0;
			int numRows = 0;
			boolean lastWasParagraph = false;

			Dimension[] sizes = new Dimension[count];

			// First pass: work out the column widths and row heights
			for (int i = 0; i < count; i++) {
				Component c = target.getComponent(i);
				if (includeComponent(c)) {
					Dimension d = getComponentSize(c, type);
					int w = d.width;
					int h = d.height;
					sizes[i] = d;
					Integer n = (Integer)getConstraint(c);

					if (i == 0 || n == NEW_PARAGRAPH || n == NEW_PARAGRAPH_TOP) {
						if (i != 0)
							y += rowHeight+vGapMajor;
						colWidth1 = Math.max(colWidth1, w);
						colWidth = 0;
						rowHeight = 0;
						lastWasParagraph = true;
					} else if (n == NEW_LINE || lastWasParagraph) {
						x = 0;
						if (!lastWasParagraph)
							y += rowHeight+vGapMinor;
						colWidth = w;
						colWidth2 = Math.max(colWidth2, colWidth);
						if (!lastWasParagraph)
							rowHeight = 0;
						lastWasParagraph = false;
					} else {
						colWidth += w+hGapMinor;
						colWidth2 = Math.max(colWidth2, colWidth);
						lastWasParagraph = false;
					}
					rowHeight = Math.max(h, rowHeight);
				}
			}

			if(requestedFirstColumnWidth > 0)
			{
				colWidth1 = requestedFirstColumnWidth;
			}

			if (dimension != null) {
				dimension.width = colWidth1 + hGapMajor + colWidth2;
				dimension.height = y + rowHeight;
			} else {
				x = 0;
				y = 0;
				lastWasParagraph = false;
				int start = 0;
				Integer paragraphType = NEW_PARAGRAPH;

				boolean firstLine = true;
				for (int i = 0; i < count; i++) {
					Component c = target.getComponent(i);
					if (includeComponent(c)) {
						Dimension d = sizes[i];
						int w = d.width;
						int h = d.height;
						Integer n = (Integer)getConstraint(c);

						if (i == 0 || n == NEW_PARAGRAPH || n == NEW_PARAGRAPH_TOP) {
							paragraphType = n;
							if (i != 0)
								layoutRow(target, sizes, start, i-1, y, rowHeight, firstLine, type, paragraphType);
							start = i;
							firstLine = true;
							if (i != 0)
								y += rowHeight+vGapMajor;
							rowHeight = 0;
							lastWasParagraph = true;
						} else if (n == NEW_LINE || lastWasParagraph) {
							if (!lastWasParagraph) {
								layoutRow(target, sizes, start, count-1, y, rowHeight, firstLine, type, paragraphType);
								start = i;
								firstLine = false;
							}
							if (!lastWasParagraph)
								y += rowHeight+vGapMinor;
							if (!lastWasParagraph)
								rowHeight = 0;
							lastWasParagraph = false;
						} else
							lastWasParagraph = false;
						rowHeight = Math.max(h, rowHeight);
					}
				}
				layoutRow(target, sizes, start, count-1, y, rowHeight, firstLine, type, paragraphType);
			}
		}

	}

	protected void layoutRow(Container target, Dimension[] sizes, int start, int end, int y, int rowHeight, boolean paragraph, int type, Integer paragraphType) {
		int x = 0;
		Insets insets = target.getInsets();
		for (int i = start; i <= end; i++) {
			Component c = target.getComponent(i);
			if (includeComponent(c)) {
				Dimension d = sizes[i];
				int w = d.width;
				int h = d.height;

				if (i > 0 || !isFirstFieldOutdented) {
					if (i == start) {
						if (paragraph)
							x = colWidth1-w;
						else
							x = colWidth1 + hGapMajor;
					} else if (paragraph && i == start+1) {
						x = colWidth1 + hGapMajor;
					}
				}
				int yOffset = paragraphType == NEW_PARAGRAPH_TOP ? 0 : (rowHeight-h)/2;
				c.setBounds(insets.left+hMargin+x, insets.top+vMargin+y+yOffset, w, h);
				x += w + hGapMinor;
			}
		}
	}
}
