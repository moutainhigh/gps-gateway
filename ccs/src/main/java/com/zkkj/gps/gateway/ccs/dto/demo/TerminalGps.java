package com.zkkj.gps.gateway.ccs.dto.demo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;


@Data
public class TerminalGps {

    public static List<TerminalGpsBean> terminalGpsBeans;

    static {

        terminalGpsBeans = Lists.newArrayList();

        List<String> terminalLists = Lists.newArrayList();
        terminalLists.add("26043411039");//109.393737  38.065754
        terminalLists.add("57047080301");//109.513319  38.042569
        terminalLists.add("26043230827");//109.369378  38.025176
        terminalLists.add("57047077992");//109.519643,lat:38.15933
        terminalLists.add("26043371415");//109.372252,lat:38.044275
        terminalLists.add("26043186219");//109.546696,lat:38.158385
        terminalLists.add("016600000058");//109.446696,lat:38.557385
        terminalLists.add("57047081937");//109.428594,lat:38.098362
        terminalLists.add("26043379673");//109.464814,lat:38.1238
        terminalLists.add("26043376463");//109.453315,lat:38.11835
        terminalLists.add("26043165882");//109.49126,lat:38.139241
        terminalLists.add("26043380275");//109.428232,lat:38.099383
        terminalLists.add("26043488821");//109.433193,lat:38.058369
        terminalLists.add("57047156713");//109.643038,lat:38.154905
        terminalLists.add("57047088650");//109.479399,lat:38.012098
        terminalLists.add("57047077414");//109.413071,lat:38.033362
        terminalLists.add("26043227740");//109.402361,lat:38.033929
        terminalLists.add("26043488482");//109.420545,lat:38.091546
        terminalLists.add("26043378493");//109.391225,lat:38.022447

        List<Double> lonList = Lists.newArrayList();
        lonList.add(109.393737);lonList.add(109.513319);lonList.add(109.369378);
        lonList.add(109.519643);lonList.add(109.372252);lonList.add(109.546696);
        lonList.add(109.446696);lonList.add(109.428594);lonList.add(109.464814);
        lonList.add(109.453315);lonList.add(109.49126);lonList.add(109.428232);
        lonList.add(109.433193);lonList.add(109.643038);lonList.add(109.479399);
        lonList.add(109.413071);lonList.add(109.402361);lonList.add(109.420545);lonList.add(109.391225);
        List<Double> latList = Lists.newArrayList();
        latList.add(38.065754);latList.add(38.042569);latList.add(38.025176);latList.add(38.15933);
        latList.add(38.044275);latList.add(38.158385);latList.add(8.557385);latList.add(8.098362);latList.add(38.1238);
        latList.add(38.11835);latList.add(38.139241);latList.add(38.099383);latList.add(38.058369);latList.add(38.154905);
        latList.add(38.012098);latList.add(38.033362);latList.add(38.033929);
        latList.add(38.091546);latList.add(38.022447);

        TerminalGpsBean terminalGpsBean;
        for (int i = 0; i < terminalLists.size() ; i++) {
            terminalGpsBean = new TerminalGpsBean(terminalLists.get(i),latList.get(i),lonList.get(i));
            terminalGpsBeans.add(terminalGpsBean);
        }

    }

}
