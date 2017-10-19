package com.pay.alipay.utils;

/**
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 * 
 * @author Administrator
 * 
 */
public class AlipayConfig {
	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
	public static String partner = "2**************1";

	// 商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+0EH7pbvqasw5A8Mgic6t8/MUvayMfYdOllSZq7/fTO3fwZ0HhIVPdY4ifQNCZ61mE6G3wY79B5I8wpFuRF9LZ8J7C/4XSi/uKUUhQVj1etrBX4FsBQ64Orj9KcaJS2Mn81GQw7I6jdZ2hIT7Kn7aaDNxiC3sonRzMTdkOFr49VZAjmLAQaqHezl8jJIRrVYnBZF/weqNp4/lEyV8o5KBEasrj42Ld044ru2EHd7LRvWrxlAGiCeR7aKSPlgm+lqfDOiKssO24BhpFyORsGY+Mn3AZcWCcnNfzp/L5V/yub/ImkIn5EtqB62mhRu56VWaIcpUSz/mvuXllebOG8OJAgMBAAECggEAOPwQ+IpCWHuUieJpv6noI2MbUTJj+YDzi7Cxi0MThih/UbeModYdyeEdlYcoFgjIbBeo0Cvp+/7q2WJx9DBPFUHjfsPSGjp5nfKNRqUxX/5UnjNbf1rzTmmbHWAAfrgY4LF1xnnrHDmHttVUsX27bJZ55on4zRanGE+2byO1Z2u49wJ/2xkw2uNMy5Ax45xtMasngbeoYpC+jCMQMERee36ABbqV7X4n/0/IEXSumtfWEg1kUb2kKqDyy1gTmMVeWGNmSvhAT/2vI0D9Hym5DnP1H5Iq1LaJig89BOTyjBQmYUEpl5G0Wkj2EBrKbo8ZI6pbl2tKh+WMRXhmRq0vEQKBgQDuYfhtKMe1YAsxzRsWC7gCmwhIUAEa02ftFXEikC22kc2KDcocdIlr1Vw4y+Oz7/S19XPhlJnvOjBuMpaJJXnl5Y9F0WTcBKWghkcX/XIr6Xclr7IxZi0iVKRVC7GgR8JTAkrDew/SkX840OU+MtFTNcr7xR21j2+nWBQ941YNhwKBgQDM6k/4cslhJAjpgwLPNstoZRyBXc4tgfAYd2af1sryfHbH9ewBfaGv/CbRX+bfrfsFJE4O0OrYwE+7uIJPLYrTqWfjTr+NfnVJd1iy//v9ApOjkZLztosiQxWwqu9FBGXW6a9BPgazLq+xCln8RiUlQMSwFgUtK58iXYVF8L5qbwKBgQDn8c19sPs4YH4j89TSC478ggp6CdY5Ws+5IG7XWYARLybVl/s62q1Hje9QmBozX1w0m+In8KYi3fR3lKNiSGOmLEnOfiJr7d2WJnEQR5uKXHyA38Y7SJbL252V3m3zbT8H86f5Mkk/8i/egWs8rMd99k5N0QrKOQlDw3DWkteNCwKBgGQPntFRK+jVccyk/sA0n4rwpMS/C6jJJHauB1zRw/Q4tr/Z5E6uDEAlPsdKrzBWgpb2LW+qsKL69XRTGr6THJFSzbn/gCYVtCwVrShrO59GnNVohnufjb1DSNol/gSTqy8QmVWgdiEVERHAXkSD9CuGvwTFG+38uQ5M4IFQl4rdAoGAYpOPMONelR1m1CZbo+9b48SWAYtWzCSSKJiQhp7TAMBLR9oejRN0CymEuBwtuBz1OU8xI49ns/yxijYqu4tdvLXLFH9yKfYFTyxPNKujhS/4V7fHBV9B7VdV2C8yPtQ/Q/8CfaIiMkYZ0qkgc97+jlaP+aUOJnrXPCc0XKSVQyY=";

	// 支付宝的公钥，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
	public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvtBB+6W76mrMOQPDIInOrfPzFL2sjH2HTpZUmau/30zt38GdB4SFT3WOIn0DQmetZhOht8GO/QeSPMKRbkRfS2fCewv+F0ov7ilFIUFY9XrawV+BbAUOuDq4/SnGiUtjJ/NRkMOyOo3WdoSE+yp+2mgzcYgt7KJ0czE3ZDha+PVWQI5iwEGqh3s5fIySEa1WJwWRf8HqjaeP5RMlfKOSgRGrK4+Ni3dOOK7thB3ey0b1q8ZQBognke2ikj5YJvpanwzoirLDtuAYaRcjkbBmPjJ9wGXFgnJzX86fy+Vf8rm/yJpCJ+RLagetpoUbuelVmiHKVEs/5r7l5ZXmzhvDiQIDAQAB";

	// 签名方式
	public static String sign_type = "RSA";

	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "C://";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

	// 接收通知的接口名
	public static String service = "http://60.***.***.00/callbacks.do";
	// public static String service = "mobile.securitypay.pay";

	// APPID
	public static String app_id = "2017042006835717";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
