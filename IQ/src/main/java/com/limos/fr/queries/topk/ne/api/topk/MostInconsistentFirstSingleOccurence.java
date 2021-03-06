package com.limos.fr.queries.topk.ne.api.topk;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.limos.fr.mod.Config;

public class MostInconsistentFirstSingleOccurence{

	private Parameters param;
	public void setParam(Parameters param) {
		this.param = param;
	}
	public Parameters getParam() {
		return param;
	}
		
	public void algorithm() throws Exception {
		param.loadLattices();
			int constraintSize = Long.bitCount(param.dcs);
			String space = "";
			for (List<Long> e:param.lattices)
				space += "x"+e.size();
			System.out.println(space.substring(1)+" dc="+constraintSize);
			for (int i = constraintSize; i>=0; i--) {
				effectiveJoin(new ArrayList<List<Long>>(param.lattices), new ArrayList<Long>(), 0l, i);
				if (param.results.size()==param.k) 
					break;
			}
	}
		
	public void effectiveJoin(List<List<Long>> current, List<Long> list, long cumul, int nbrViol) throws Exception {
		List<Long> l0 = current.remove(0);
		for (long l:l0) {
			if (Long.bitCount(l) <= nbrViol) {
				List<Long> ll = new ArrayList<Long>(list);
				ll.add(l);
				long c = (cumul|l);
				if (current.isEmpty()) {
					if (Long.bitCount(c) == nbrViol) { 
						doJoin(ll, c);
						if (param.results.size()==param.k)
							return;
					}
					continue;
				}
				List<List<Long>> current1 =  new ArrayList<List<Long>>();
				for(List<Long> l_:current) {
					List<Long> l_l = new ArrayList<Long>(l_);
					current1.add(l_l);
				}
				effectiveJoin(current1, ll, c, nbrViol);
				if (param.results.size()==param.k)
					return;
			}
		}
	}
	
	int iterate = 0;
	String plans = "";
	
	private void doJoin(List<Long> list, long violation) throws Exception {
		String from = "";
		String where = param.predicate;
		int remaind = param.k - param.results.size();
		String inc = "";
		
		for (int i = 0; i<list.size(); i++) {
			String rel = param.relations_names.get(i).split("_")[0];
			String label = param.relations_names.get(i).split("_")[1];
			
			//inc += " AND " +label+".vioset="+list.get(i);
			inc += " AND ("+ param.aliasVioset(label +".vioset", list.get(i))+")";
			
			from += ", "+rel+" "+label;
		}
		String wh = "";
		if ((where.replaceAll("( )*", "").replaceAll("(\n)*", "").replaceAll("(\t)*", "")).isEmpty())
			wh = inc.substring(6);
		else
			wh = where + inc;
		String query = "SELECT "+param.attributs+" FROM "+from.substring(2) + " WHERE "+ wh + " LIMIT "+remaind;
		ResultSet resSets = Config.getCon().createStatement().executeQuery(query);
		//load columns
		if (param.attrsRes==null || param.attrsRes.isEmpty()) {
			param.attrsRes = new ArrayList<>();
			param.attrsLabelsRes = new ArrayList<String>();
			param.typeRes = new ArrayList<String>();
			for(int i =1; i<=resSets.getMetaData().getColumnCount(); i++){
				param.attrsRes.add(resSets.getMetaData().getColumnName(i));
				param.attrsLabelsRes.add(resSets.getMetaData().getColumnLabel(i));
				param.typeRes.add(resSets.getMetaData().getColumnTypeName(i));
			}
			param.attrsRes.add("Violation");
			param.attrsLabelsRes.add("Violation");
			
			param.attrsRes.add("Nb Vio");
			param.attrsLabelsRes.add("Nb Vio");
		}
		//Load Answers
		if (param.results==null)
			param.results = new ArrayList<List<String>>();
		while(resSets.next()) {
			List<String> temp = new ArrayList<String>();
			for(int i =1; i<=resSets.getMetaData().getColumnCount(); i++) {
				temp.add(resSets.getString(i));
			}
			temp.add(violation+"");
			temp.add(Long.bitCount(violation)+"");
			param.results.add(temp);
		}
	}	
		
}