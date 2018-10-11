/**
 * Created by chenshaobo@syswin.com on 2017/6/5.
 */
window.CONFIG = {
  // 是否开发环境
  DEV: false,
  // 本地打桩数据接口 ，正式环境建议置空
  // API_DEV:"/static/data",
  //后台接口地址
  API_HOST: "//t100chendeticket.zhengtoon.com",
}

/**
 * 执行函数
 */
window.onload=function(){
  //手机控制台
  // erudaConsole()
}




/**
 * eruda【手机控制台】
 * 此方法不允许打包到项目中去，请在config.js中使用
 * 仅在开发环境允许使用，测试和正式环境请勿出现此代码
 */
function erudaConsole(){
  var erudaScript = document.createElement('script');
  erudaScript.type = 'text/javascript';
  erudaScript.src  ='//cdn.bootcss.com/eruda/1.3.0/eruda.min.js';
  document.body.appendChild(erudaScript);
  erudaScript.onload = function(){ eruda.init() }
}
