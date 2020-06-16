#!/usr/bin/python3
#coding=utf-8
import re
import os 
import json
import sys
import requests

def getContent():
    #获取提交记录信息
    content = (os.popen("git log --pretty=format:\"%an-&%h-&%s\" -1 ").read()).split("-&")
    return content


def postDingDing(content):
    print(sys.argv)
    project_url = sys.argv[1]
    project_name = sys.argv[2]
    commit_url = sys.argv[3]
    msg=sys.argv[4]
    name,shortcodenum,explain = content
     
    dingdingurl = ["https://oapi.dingtalk.com/robot/send?access_token=33f3b7e01b6625ce875a68eb9705fc6d4f325f15a47a27a64692e278f2bf6fe3"]
    headers = {'Content-Type': 'application/json'}
    String_textMsg = {
        "msgtype": "markdown",
        "markdown": {
            "title" : "commit_auto_test",
            "text":   "#### "+name+" 推送  ["+project_name+"]("+project_url+") **"+msg+"** \n  点击[提交地址]("+commit_url+")查看提交内容"+" \n "+explain
        }
    }
    requests.packages.urllib3.disable_warnings()
    String_textMsg = json.dumps(String_textMsg)
    for i in dingdingurl:
        requests.post(i, data=String_textMsg, headers=headers, verify=False)
    print(name,shortcodenum,explain,project_url,project_name,commit_url,msg)



if __name__ == "__main__":
    content = getContent()
    print(content)
    postDingDing(content)