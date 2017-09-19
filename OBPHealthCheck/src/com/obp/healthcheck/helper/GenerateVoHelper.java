package com.obp.healthcheck.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class GenerateVoHelper {

	public HashMap<String, HashMap<String, String>> getConsoleMapData(List<String> list) {
		HashMap<String, HashMap<String, String>> res = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> tmp = new HashMap<String, String>();
		for (String str : list) {
			String[] parts = str.split("::?");
			if (parts[0].equals("ENVNAME")) {
				res.put(parts[1].trim(), tmp);
				tmp = new HashMap<String, String>();
				continue;
			}
			tmp.put(parts[0].trim(), parts[1].trim());
		}
		return res;
	}

	public HashMap<String, ArrayList<ArrayList<String>>> getConsoleListData(List<String> list) {
		System.out.println("calling getConsoleDsData");
		HashMap<String, ArrayList<ArrayList<String>>> res = new HashMap<String, ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<String>> tmp = new ArrayList<ArrayList<String>>();
		ArrayList<String> tmp1 = new ArrayList<String>();
		for (String str : list) {
			tmp1 = new ArrayList<String>();
			String[] parts = str.split("::?");
			if (parts[0].equals("ENVNAME")) {
				res.put(parts[1].trim(), tmp);
				tmp = new ArrayList<ArrayList<String>>();
				continue;
			}
			for (String s : parts) {
				tmp1.add(s.trim());
			}
			tmp.add(tmp1);
		}
		return res;
	}

	public HashMap<String, LinkedHashSet<ArrayList<String>>> getConsoleHTListData(List<String> list) {
		System.out.println("calling getConsoleHeapThreadData");
		HashMap<String, LinkedHashSet<ArrayList<String>>> res = new HashMap<String, LinkedHashSet<ArrayList<String>>>();
		LinkedHashSet<ArrayList<String>> tmp = new LinkedHashSet<ArrayList<String>>();
		ArrayList<String> tmp1 = new ArrayList<String>();
		for (String str : list) {
			String[] parts = str.split("::?");
			if (parts[0].equals("No stack trace available.")) {
				continue;
			}
			if (parts[0].equals("ENVNAME")) {
				res.put(parts[1].trim(), tmp);
				tmp = new LinkedHashSet<ArrayList<String>>();
				continue;
			}
			// for (String s : parts)
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].trim().equals("APNDLN")) {
					continue;
				}
				if ((parts[0].trim().equals("APNDLN")) && (i == 2 || i == 3)) {
					BigDecimal bd = new BigDecimal(parts[i].trim());
					BigDecimal gb = new BigDecimal(1073741824);
					BigDecimal gbres = bd.divide(gb, 2, BigDecimal.ROUND_HALF_UP);
					tmp1.add(String.valueOf(gbres));
					continue;
				}
				tmp1.add(parts[i].trim());
			}
			if (parts[0].trim().equals("APNDLN")) {
				tmp.add(tmp1);
				continue;
			}
			tmp1 = new ArrayList<String>();
		}
		return res;
	}

	public HashMap<String, ArrayList<ArrayList<String>>> getConsoleAppServerData(List<String> list) {
		HashMap<String, ArrayList<ArrayList<String>>> res = new HashMap<String, ArrayList<ArrayList<String>>>();
		ArrayList<String> tmp = new ArrayList<String>();
		ArrayList<String> tmp1 = new ArrayList<String>();
		ArrayList<ArrayList<String>> tmpres = new ArrayList<ArrayList<String>>();
		int scount = 0, acount = 0;
		for (String str : list) {
			String[] parts = str.split("::?");

			if (parts[0].equals("ENVNAME")) {
				res.put(parts[1].trim(), tmpres);
				tmp = new ArrayList<String>();
				tmp1 = new ArrayList<String>();
				tmpres = new ArrayList<ArrayList<String>>();
				scount = 0;
				acount = 0;
				continue;
			}
			if (parts[0].equals("SVRAPPDTLS")) {
				if (acount < scount) {
					tmp1 = tmpres.get(acount);
					tmp1.add(parts[1].trim());
					tmp1.add(parts[2].trim());
					tmpres.remove(acount);
					tmpres.add(acount++, tmp1);
				} else {
					tmp1 = new ArrayList<>();
					tmp1.add(null);
					tmp1.add(null);
					tmp1.add(parts[1].trim());
					tmp1.add(parts[2].trim());
					tmpres.add(tmp1);
				}

			} else {
				tmp = new ArrayList<>();
				tmp.add(parts[0].trim());
				tmp.add(parts[1].trim());
				tmpres.add(tmp);
				scount++;
			}

		}
		return res;
	}

}
