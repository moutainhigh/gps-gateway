package com.zkkj.gps.gateway.ccs.controller;

import com.zkkj.gps.gateway.ccs.websocket.WebSocketAlarmInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-05-09 上午 10:13
 */
@Controller
@RequestMapping("/websocket")
@CrossOrigin
@Api(value = "WebSocket示例", description = "WebSocket")
public class WebSocketController {

    @ApiOperation(value = "打开客户端页面！",notes = "说明信息！")
    @GetMapping(value = {"/", "index.html"})
    public String index(){
        return "wstest";
    }

    /*@ApiOperation(value = "打开客户端页面！",notes = "说明信息！")
    @GetMapping(value = {"/", "test.html"})
    public String test(){
        return "test";
    }*/

    @ApiOperation(value = "服务端向客户端单发消息！",notes = "说明信息！")
    @GetMapping(value = "/sendMessage")
    @ResponseBody
    public String sendMessage(@RequestParam String message,@RequestParam String id){
        WebSocketAlarmInfo.sendMsg(message,id);
        return "success";
    }

    /**
     * 推送给所有在线用户
     * @return
     */
    @ApiOperation(value = "服务端向客户端群发消息！",notes = "说明信息！")
    @GetMapping(value = "/sendAll")
    @ResponseBody
    public String sendAll(String msg){
        WebSocketAlarmInfo.sendAll(msg);
        return "success";
    }

}
