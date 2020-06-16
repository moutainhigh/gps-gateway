package com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpssubscribe;

import com.zkkj.gps.gateway.terminal.monitor.algorithm.TerminalAlarmAlgorithm;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaPointsDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.GraphTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import com.zkkj.gps.gateway.terminal.monitor.utils.CoordinateUtilTest;
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueList;
import org.greenrobot.eventbus.EventBus;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * GpsListInfoAlarmSubscribe Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>四月 19, 2019</pre>
 */
public class GpsInfoAlarmSubscribeTest extends CoordinateUtilTest {

    /**
     * Method: subscribe(GpsListInfoEvent gpsInfoEvent)
     */
    @Test
    public void testSubscribe() throws Exception {
        //TODO: Test 报警处理订阅事件
    }


    /**
     * Method: terminalMonitor(QueueList<GPSPositionDTO> hisListPosition, AppKeyAlarmConfigDto appKeyAlarmConfig)
     */
    @Test
    public void testTerminalMonitor() throws Exception {
        //TODO: Test 终端监控
        /*
        try {
           Method method = GpsListInfoAlarmSubscribe.getClass().getMethod("terminalMonitor", QueueList<GPSPositionDTO>.class, AppKeyAlarmConfigDto.class);
           method.setAccessible(true);
           method.invoke(<Object>, <Parameters>);
        } catch(NoSuchMethodException e) {
        } catch(IllegalAccessException e) {
        } catch(InvocationTargetException e) {
        }
        */
    }

    /**
     * Method: getTerminalAlarmConfig(String terminalId)
     */
    @Test
    public void testGetTerminalAlarmConfig() throws Exception {
        //TODO: Test 获取报警配置
        /*
        try {
           Method method = GpsListInfoAlarmSubscribe.getClass().getMethod("getTerminalAlarmConfig", String.class);
           method.setAccessible(true);
           method.invoke(<Object>, <Parameters>);
        } catch(NoSuchMethodException e) {
        } catch(IllegalAccessException e) {
        } catch(InvocationTargetException e) {
        }
        */
    }


    @Test
    public void testGpsListInfoAlarmSubscribe() {

        try {
            //将对应的信息数据转化为对应的坐标点信息
            List<Point2D.Double> doubleList = returnPoint();
            StringBuffer stringBuffer = new StringBuffer();
            QueueList<BasicPositionDto> hisListPositionAll = new QueueList<>(doubleList.size());
            LocalDateTime of = LocalDateTime.of(2019, 05, 01, 18, 07, 27);
            for (int i = 0; i < doubleList.size(); i++) {
                //new BMap.Point(116.399, 39.910),
                //Append_33 append_33 = getAppend(i);
//                Append_33 append_33 = new Append_33();
//                append_33.setAntiDismantle("1");
//                append_33.setPower("419");
//                stringBuffer.append("new BMap.Point(" + doubleList.get(i).y + "," + doubleList.get(i).x + "),");
//                SiteBasicMessage siteBasicMessage = new SiteBasicMessage(0, 0, doubleList.get(i).x, doubleList.get(i).y, 9, 0, 0, of);
//                of = of.plusMinutes(2);
//                if (of.isAfter(DateTimeUtils.parseLocalDateTime("2019-05-08 21:25:27")) && of.isBefore(DateTimeUtils.parseLocalDateTime("2019-05-11 06:25:27"))) {
//                    siteBasicMessage.setSpeed(1);
//                }
//                if (i == doubleList.size() - 1) {
//                    append_33.setAntiDismantle("0");
//                    append_33.setPower("420");
//                }
//                hisListPositionAll.add(new GPSPositionDTO(siteBasicMessage, new SiteAppendMessage(append_33), of));
            }
//            String str = stringBuffer.toString();
//            String terminalId = "064768104257";
//            //添加对应的区域配置信息
//            List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = new ArrayList<>();
//            //设置违规区域1（关庄镇）
//            AreaDto area1 = new AreaDto();
//            AreaPointsDto arrPoints[] = new AreaPointsDto[5];
//            arrPoints[0] = new AreaPointsDto(0, 34.973630204372800, 108.872339136480000);
//            arrPoints[1] = new AreaPointsDto(1, 34.945111432659100, 108.864031899243000);
//            arrPoints[2] = new AreaPointsDto(2, 34.944526898418000, 108.890953413997000);
//            arrPoints[3] = new AreaPointsDto(3, 34.971957539605700, 108.892969386802000);
//            arrPoints[4] = new AreaPointsDto(4, 34.971957539605700, 108.892969386802000);
//            area1 = new AreaDto(GraphTypeEnum.POLYGON, "1", "关庄镇", "陕西省铜川市耀州区咸丰路街道旬耀路",
//                    108.873852685785000, 34.960118908346400, 500, arrPoints);
//            //设置违规区域2（玉皇阁大桥）
//            AreaDto area2 = new AreaDto();
//            AreaPointsDto yuhuangge[] = new AreaPointsDto[5];
//            yuhuangge[0] = new AreaPointsDto(0, 34.875375446400100, 108.904897189036000);
//            yuhuangge[1] = new AreaPointsDto(1, 34.871483624501500, 108.919197581178000);
//            yuhuangge[2] = new AreaPointsDto(2, 34.853985777532100, 108.912117051224000);
//            yuhuangge[3] = new AreaPointsDto(3, 34.859407401485500, 108.894314570340000);
//            yuhuangge[4] = new AreaPointsDto(4, 34.875404970608500, 108.904969187247000);
//            area2 = new AreaDto(GraphTypeEnum.POLYGON, "2", "玉皇阁大桥", "陕西省铜川市耀州区咸丰路街道旬耀路",
//                    108.902111598133000, 34.865849624008700, 500, yuhuangge);
//            //设置违规区域3（延西高速）
//            AreaDto area3 = new AreaDto();
//            AreaPointsDto yanxigs[] = new AreaPointsDto[5];
//            yanxigs[0] = new AreaPointsDto(0, 34.876961141383700, 108.911307797120000);
//            yanxigs[1] = new AreaPointsDto(1, 34.869565892141200, 108.908781354752000);
//            yanxigs[2] = new AreaPointsDto(2, 34.869130510748700, 108.910545391495000);
//            yanxigs[3] = new AreaPointsDto(3, 34.876250859668200, 108.913433336612000);
//            yanxigs[4] = new AreaPointsDto(4, 34.876872078048800, 108.911307675660000);
//            area3 = new AreaDto(GraphTypeEnum.POLYGON, "3", "延西高速", "陕西省铜川市耀州区咸丰路街道旬耀路",
//                    108.906457584159000, 34.874189796663700, 500, yanxigs);
//            //设置违规区域4（延西高速玉皇阁大桥交叉点--圆形）
//            AreaDto area4 = new AreaDto(GraphTypeEnum.CIRCLE, "4", "延西高速玉皇阁大桥交叉点", "陕西省铜川市耀州区咸丰路街道G65W延西高速",
//                    108.911702240905000, 34.874323263616600, 600, null);
//            //设置违规区域4（延西高速玉皇阁大桥交叉点--圆形）
//            AreaDto area5 = new AreaDto(GraphTypeEnum.CIRCLE, "5", "沿河湾镇", "陕西省铜川市耀州区咸丰路街道G65W延西高速",
//                    109.3673310136986, 36.757171672067784, 400, null);
//
//            //进区域
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "1", AlarmTypeEnum.VIOLATION_AREA, 2, "华能铜川照金电厂", "91610000786990367Y", area1, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null, null,1
//            )));
            /*appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
                    "1", AlarmTypeEnum.STOP_OVER_TIME, 2, "华能铜川照金电厂", "91610000786990367Y", area1, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null
            )));
            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
                    "2", AlarmTypeEnum.STOP_OVER_TIME, 2, "华能铜川照金电厂", "91610000786990367Y", area2, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null
            )));*/


            /*appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
                    "45", AlarmTypeEnum.STOP_OVER_TIME, 20, "华能铜川照金电厂", "91610000786990367Y", null, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null
            )));*/


           /* appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
                    "1", AlarmTypeEnum.VIOLATION_AREA, 0, "华能铜川照金电厂", "91610000786990367Y", area1, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null
            )));*/
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "2", AlarmTypeEnum.VIOLATION_AREA, 0, "华能铜川照金电厂", "91610000786990367Y", area2, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null,null
//            ,0)));
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "3", AlarmTypeEnum.VIOLATION_AREA, 0, "华能铜川照金电厂", "91610000786990367Y", area3, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null,null
//            ,0)));
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "4", AlarmTypeEnum.VIOLATION_AREA, 0, "华能铜川照金电厂", "91610000786990367Y", area4, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null,null
//            ,0)));
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "5", AlarmTypeEnum.VIOLATION_AREA, 0, "华能铜川照金电厂", "91610000786990367Y", area5, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null,null
//            ,0)));
//
//
//            //区域超速
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "12", AlarmTypeEnum.OVER_SPEED, 2, "华能铜川照金电厂", "91610000786990367Y", area1, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null,null
//            ,0)));
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "13", AlarmTypeEnum.OVER_SPEED, 2, "华能铜川照金电厂", "91610000786990367Y", area2, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null,null
//            ,0)));
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "14", AlarmTypeEnum.OVER_SPEED, 2, "华能铜川照金电厂", "91610000786990367Y", area3, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null,null
//            ,0)));
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "15", AlarmTypeEnum.OVER_SPEED, 2, "华能铜川照金电厂", "91610000786990367Y", area4, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null,null
//            ,0)));
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "16", AlarmTypeEnum.OVER_SPEED, 2, "华能铜川照金电厂", "91610000786990367Y", area5, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null,null
//            ,0)));
//
//            //防拆
//            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
//                    "17", AlarmTypeEnum.EQUIP_REMOVE, 1, "华能铜川照金电厂", "91610000786990367Y", null, "2019-05-01 18:07:27", "2019-05-09 06:08:00", null,null
//            ,0)));


            //低电量
           /* appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
                    "22", AlarmTypeEnum.LOW_POWER, 420, "华能铜川照金电厂", "91610000786990367Y", area1, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null
            )));

            //区域停车超时
            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
                    "24", AlarmTypeEnum.STOP_OVER_TIME, 3, "华能铜川照金电厂", "91610000786990367Y", area1, "2019-05-01 18:07:27", "2019-06-02 06:08:00", null
            )));
            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
                    "25", AlarmTypeEnum.STOP_OVER_TIME, 3, "华能铜川照金电厂", "91610000786990367Y", area2, "2019-05-01 18:07:27", "2019-06-06 06:08:00", null
            )));*/


            //添加报警线路
            /*LineStringDto[] lineStrings = new LineStringDto[1];
            String routeStr = "34.9769212803632,108.889695499182;34.9769146545139,108.88970131684;34.9765029809028,108.889098344184;34.9760303424479,108.888450673177;34.9753555269097,108.88755768533;34.9710357999132,108.882018964844;34.970513718316,108.881362889323;34.9691209474826,108.879577557292;34.9686522400174,108.87895950434;34.9681832612847,108.878356260417;34.9681832612847,108.878356260417;34.9680073138021,108.878066245226;34.9678012057292,108.877769011719;34.9673823138021,108.877249946181;34.9666073246528,108.876349740017;34.966218593316,108.875868425781;34.9659862556424,108.875548521267;34.9658181701389,108.875265181858;34.9656693671875,108.874998108941;34.9654780681424,108.874585892795;34.9653709474826,108.874304010851;34.9652867690972,108.874036666667;34.9651953723958,108.873601508247;34.9651383385417,108.873082171441;34.9651260030382,108.872815098524;34.9651417269965,108.872510003038;34.9651750047743,108.872173560764;34.9652317673611,108.87185365625;34.9653072699653,108.871548289497;34.965407172309,108.871250141059;34.9655246284722,108.870952907552;34.9656652981771,108.870654759115;34.965794547309,108.870433570747;34.9660948980035,108.869968251736;34.9661746028646,108.86985299783;34.9668218324653,108.868944387153;34.9674916319444,108.868403023003;34.9678311914063,108.868127545573;34.9684548350694,108.867631422309;34.9690144978299,108.867157698785;34.9693918085938,108.866814580729;34.9697948064236,108.866409311632;34.9701368394097,108.866042708767;34.9704571163194,108.865653434896;34.9704990698785,108.865592470052;34.9707613980035,108.865256299045;34.9710195238715,108.864881562934;34.9712936449653,108.864446404514;34.9717839383681,108.863545655816;34.9719970946181,108.863103007813;34.9724076831597,108.862132889757;34.9728137981771,108.861093944878;34.9728137981771,108.861093944878;34.9735740104167,108.859688397135;34.9738659565972,108.85916997526;34.974140077691,108.858749354601;34.9743761762153,108.858420774306;34.9746424353299,108.858092465278;34.9748021163194,108.857916246528;34.9748860234375,108.857817259115;34.9750602421875,108.85765703559;34.9752319874132,108.857526701389;34.9754067486979,108.857404771701;34.9755935742187,108.857313375;34.9757801284722,108.857236414931;34.9760237165799,108.857168503038;34.9767436028646,108.856992284288;34.976980515191,108.856907834635;34.9772657855903,108.856794038194;34.9775283849826,108.856648623698;34.9779205047743,108.85642743533;34.9781182083333,108.856266025608;34.9784574965278,108.856098855035;34.9786971536458,108.855992005642;34.9788348072917,108.855914503038;34.9789986905382,108.855831510851;34.9791581002604,108.855724390191;34.9791581002604,108.855724390191;34.9792420073785,108.855663425347;34.979774796875,108.855242804688;34.9800872118056,108.855021616319;34.9802197482639,108.854960651476;34.9803115173611,108.854922357639;34.9804485273437,108.854906362413;34.9805783190104,108.854906362413;34.9807464045139,108.854945299913;34.9808641319444,108.854998402778;34.9809821306424,108.855059638889;34.9810470264757,108.855112470486;34.9812380542535,108.855288689236;34.9814634448785,108.855533091146;34.9816312591146,108.855708123698;34.981829233941,108.855884342448;34.9818373671875,108.855846319878;34.9816614197049,108.855701176649;34.9814634448785,108.855479717014;34.9811270026042,108.855128465712;34.9809969396701,108.855013483073;34.9808789409722,108.854945299913;34.980784155816,108.854899144097;34.9807041796875,108.854868339844;34.98058225,108.854845126302;34.9804711983507,108.854845126302;34.9803226666667,108.854861121528;34.9801777947049,108.854914224392;34.9799650108507,108.855006536024;34.9794473016493,108.855250666667;34.9790591128472,108.855495068576;34.9790591128472,108.855495068576;34.9787195533854,108.855724390191;34.9781529435764,108.856151957899;34.9781529435764,108.856151957899;34.9778634709201,108.856343256944;34.9776159518229,108.856472404948;34.9773376284722,108.856594605903;34.9771127803819,108.856678784288;34.9769111458333,108.856732802083;34.9768351006944,108.856747611111;34.9766216731771,108.856794038194;34.9763898780382,108.856808847222;34.9762030525174,108.856808847222;34.9760421853299,108.856794038194;34.9758592907986,108.856747882378;34.9757183498264,108.856695050781;34.9755924891493,108.85663381467;34.9754554791667,108.85652669401;34.9753604227431,108.856434653646;34.9752415091146,108.856305505642;34.9751501124132,108.856182389757;34.9750662052951,108.856022166233;34.9749968359375,108.855861942708;34.9749509513889,108.855708666233;34.9749316688368,108.85554149566;34.9749313975694,108.855388219184;34.9749540685764,108.855243075955;34.9749957508681,108.855121146267;34.9750455664062,108.854998674045;34.9751174092882,108.854899686632;34.9752052473958,108.854807646267;34.9752701432292,108.854761490451;34.9753577100694,108.854723467882;34.9754337552083,108.854700525608;34.9754986510417,108.854693578559;34.9755825581597,108.854700525608;34.9756586032986,108.854731601128;34.9757044878472,108.854754543403;34.9757693836806,108.854807646267;34.9758333645833,108.854891824653;34.9758837226563,108.854967869792;34.9759147981771,108.855067772135;34.9759298784722,108.855144088542;34.9759370967882,108.855250937934;34.97592621875,108.855373410156;34.9758884674479,108.855510420139;34.9758319761285,108.855647701389;34.9756296979167,108.856060188802;34.9744175872396,108.857756294271;34.9738774782986,108.858665176215;34.9736262994792,108.859115686198;34.9732615954861,108.859826593316;34.9730336302083,108.860329934896;34.972623312934,108.861277110677;34.9720577881944,108.862674525174;34.9717836671007,108.863278311632;34.9715557018229,108.863744545573;34.9713123849826,108.86417906033;34.9708746519097,108.864874615885;34.9704987986111,108.865401171007;34.9704797873264,108.865424113281;34.9701407703993,108.865836600694;34.969813546441,108.866195985243;34.9695162118056,108.866508299045;34.9691090117188,108.866890625868;34.9687060138889,108.86724187717;34.9679296362847,108.86785233941;34.9676661219618,108.868043638455;34.9672477725694,108.868394889757;34.9667110520833,108.86887647526;34.966383828125,108.869196651042;34.9660336618924,108.869578062934;34.9658051540799,108.869853269097;34.9654969414063,108.870250404948;34.965100890625,108.870746799479;34.9648382912326,108.871060028212;34.9646257786458,108.871273083333;34.9644352934028,108.871433306858;34.9642067855903,108.871594445313;34.9639631974826,108.871731726562;34.9637033428819,108.87185365625;34.9634141414931,108.871998528212;34.9634141414931,108.871998528212;34.9631443237847,108.872121000434;34.9631443237847,108.872121000434;34.9624884184028,108.872335241753;34.9620930112847,108.872480113715;34.9615056614583,108.872655146267;34.9612159175347,108.872739324653;34.960892624566,108.872792427517;34.9605989496528,108.872815369792;34.959544078559,108.872892601128;34.9595322855903,108.872892601128;34.9586328932292,108.872770671441;34.9580766189236,108.872731733941;34.9575771072049,108.872708791667;34.9572912942708,108.872701573351;34.9561217118056,108.872731733941;34.9554244965278,108.872770942708;34.9554235015265,108.872843148875";
            // String routeStr="34.9769212803632,108.889695499182;34.9769146545139,108.88970131684;34.9765029809028,108.889098344184;34.9760303424479,108.888450673177;34.9753555269097,108.88755768533;34.9710357999132,108.882018964844;";
            lineStrings[0] = new LineStringDto("1", "ddd", 20.0, routeStr, false);
            appKeyAlarmConfigs.add(new AppKeyAlarmConfigDto("91610000786990367Y", new AlarmConfigDto(
                    "11", AlarmTypeEnum.LINE_OFFSET, 20, "华能铜川照金电厂", "91610000786990367Y", null, "2019-05-01 05:22:31", "2019-06-02 06:08:00", lineStrings
            )));*/

//            QueueList<BasicPositionDto> hisListPosition = new QueueList<>(5);
//            for (int i = 1; i < hisListPositionAll.size() + 1; i++) {
//                if (i % 5 == 0) {
//                    //进行报警算法处理
//                    if (appKeyAlarmConfigs != null && appKeyAlarmConfigs.size() > 0) {
//                        for (int j = 0; j < appKeyAlarmConfigs.size(); j++) {
//                            TerminalAlarmInfoDto terminalAlarmInfoDTO = null;
//                            AppKeyAlarmConfigDto appKeyAlarmConfig = appKeyAlarmConfigs.get(j);
//                            //循环调用数据点信息返回报警信息对象
//                            TerminalAlarmAlgorithm terminalAlarmAlgorithm = new TerminalAlarmAlgorithm();
//                            AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
//                            if (alarmConfig.getAlarmTypeEnum() == AlarmTypeEnum.VIOLATION_AREA || alarmConfig.getAlarmTypeEnum() == AlarmTypeEnum.OFF_LINE ||
//                                    alarmConfig.getAlarmTypeEnum() == AlarmTypeEnum.OVER_SPEED || alarmConfig.getAlarmTypeEnum() == AlarmTypeEnum.STOP_OVER_TIME ||
//                                    alarmConfig.getAlarmTypeEnum() == AlarmTypeEnum.LINE_OFFSET) {
//                                terminalAlarmInfoDTO = terminalAlarmAlgorithm.terminalAlarm(terminalId, hisListPosition, null, appKeyAlarmConfig);
//                            }
//                            if (alarmConfig.getAlarmTypeEnum() == AlarmTypeEnum.EQUIP_REMOVE) {
//                                terminalAlarmInfoDTO = terminalAlarmAlgorithm.terminalAlarm(terminalId, null, hisListPositionAll.get(j), appKeyAlarmConfig);
//                            }
//                            if (terminalAlarmInfoDTO != null) {
//
//                                AlarmInfoEvent alarmInfoEvent = new AlarmInfoEvent("91610000786990367Y", terminalAlarmInfoDTO);
//                                System.out.println("---------------------------------------------------------");
//                                EventBus.getDefault().post(alarmInfoEvent);
//                                //System.err.println(alarmInfoEvent);
//                            }
//                        }
//                    }
//                    //拿取最新的点
//
//                    hisListPosition.clear();
//                    hisListPosition.add(hisListPositionAll.get(i - 1));
//
//                } else {
//                    hisListPosition.add(hisListPositionAll.get(i - 1));
//                    for (int j = 0; j < appKeyAlarmConfigs.size(); j++) {
//                        TerminalAlarmInfoDto terminalAlarmInfoDTO = null;
//                        AppKeyAlarmConfigDto appKeyAlarmConfig = appKeyAlarmConfigs.get(j);
//                        //循环调用数据点信息返回报警信息对象
//                        TerminalAlarmAlgorithm terminalAlarmAlgorithm = new TerminalAlarmAlgorithm();
//                        AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
//                        if (alarmConfig.getAlarmTypeEnum() == AlarmTypeEnum.EQUIP_REMOVE || alarmConfig.getAlarmTypeEnum() == AlarmTypeEnum.LOW_POWER) {
//                            terminalAlarmInfoDTO = terminalAlarmAlgorithm.terminalAlarm(terminalId, null, hisListPositionAll.get(i - 1), appKeyAlarmConfig);
//                        }
//                        if (terminalAlarmInfoDTO != null) {
//                            System.err.println(terminalAlarmInfoDTO);
//                        }
//                    }
//                }
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   /* private Append_33 getAppend(int i) {
        Append_33 append_33 = new Append_33();
        append_33.setAntiDismantle("1");
        int seq = i % 1000;
        switch (seq) {
            //0正常，1是触发
            case 0:
                append_33.setAntiDismantle("1");
                append_33.setPower("422");
                break;
            case 1:
                append_33.setAntiDismantle("0");
                append_33.setPower("420");
                break;
            case 2:
                append_33.setAntiDismantle("0");
                append_33.setPower("419");
                break;
            case 3:
                append_33.setAntiDismantle("0");
                append_33.setPower("426");
                break;
            case 4:
                append_33.setAntiDismantle("0");
                append_33.setPower("419");
                break;
        }
        return append_33;
    }*/
}
