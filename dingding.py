# coding=utf-8
import re
import os
import json
import sys
import requests

status = sys.argv[1]
job_name = os.getenv("JOB_NAME")
run_display_url = os.getenv("RUN_DISPLAY_URL")
build_display_name = os.getenv("BUILD_DISPLAY_NAME")
sonar_host_url =  os.getenv("SONAR_HOST_URL")

if status == "pass":
    msg_status = "构建部署成功"
    image_url = "https://www.iconsdb.com/icons/download/lime/check-mark-3-32.png"
elif status == "fail":
    msg_status = "构建部署失败"
    image_url = "https://www.iconsdb.com/icons/download/red/x-mark-32.png"

dingdingurl = ["https://oapi.dingtalk.com/robot/send?access_token=b006c2f039560287d793d8f7e212462f541d15c32cca253fd34eee4e1009ddc0"]
headers = {'Content-Type': 'application/json;charset=UTF-8'}
String_textMsg = {
    "msgtype": "markdown",
    "markdown": {
        "title": job_name+ build_display_name,
        "text": "![]("+ image_url +")\n\n" "#### " + job_name + build_display_name + msg_status + "\n\n" "[点击]("+run_display_url+")查看此次Jenkins任务详情\n\n" "[点击]("+sonar_host_url+")查看SonarQube扫描报告"
    },
    "at": {
        "isAtAll": "false"
    }
}
requests.packages.urllib3.disable_warnings()
String_textMsg = json.dumps(String_textMsg)
for i in dingdingurl:
    requests.post(i, data=String_textMsg, headers=headers, verify=False)
print(job_name)
