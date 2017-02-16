package sample.jetty.util;

import java.util.Comparator;

import sample.jetty.domain.FolderBean;

public class OrderByComparator implements Comparator<FolderBean> {

	@Override
	public int compare(FolderBean o1, FolderBean o2) {
		if (o1.getOrderBy() < o2.getOrderBy()) {
			return 1;
		}
		return 0;
	}

}
