webpackJsonp([14],{2:function(t,e){"use strict";function n(t){$http.defaults.headers.Authorization=t}Object.defineProperty(e,"__esModule",{value:!0});var o=window.CONFIG.DEV?window.CONFIG.API_DEV:window.CONFIG.API_HOST,i=function(t){window.alert(t)};$http.defaults.headers.sessionId=window.sessionStorage.getItem("sessionId");var r={SUCCESS:"0",TIMEOUT:"500",TOKEN_INVALID:"5001",CODE_INVALID:"5002"};$http.interceptors.response.use(function(t){var e=t.meta&&t.meta.code+"";switch(e){case r.SUCCESS:return t;case r.TOKEN_INVALID:case r.CODE_INVALID:return window.sessionStorage.removeItem("sessionId"),$http.defaults.headers.sessionId=void 0,i(t.meta.message||"当前会话已过期"),setTimeout(function(){window.location.href="toon://mwap/window?params="+JSON.stringify({functionType:1})},2e3),Promise.reject(t);default:return Promise.reject(t)}},function(t){return Promise.reject(t)});e.getSessionId=function(t,e,n){function i(t,n){return window.sessionStorage.setItem("Authorization",t),$http.defaults.headers.Authorization=t,e&&e(n)}return window.sessionStorage.getItem("Authorization")?i(window.sessionStorage.getItem("Authorization")):void $http.post(o+"/v1/open/getSessionId?code="+t).then(function(t){if(0===t.meta.code)return i(t.data.tokenType+";"+t.data.accessToken,t)}).catch(function(t){return n&&n(t)})},e.serverTime=function(){return sessionStorage.getItem("Authorization")&&n(sessionStorage.getItem("Authorization")),new Promise(function(t,e){$http.get(o+"/time/range").then(function(e){t(e)}).catch(function(t){e(t)})})},e.userInfo=function(){return sessionStorage.getItem("Authorization")&&n(sessionStorage.getItem("Authorization")),new Promise(function(t,e){$http.get(o+"/user/info").then(function(e){t(e)}).catch(function(t){e(t)})})},e.ticketList=function(t,e){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(n,i){$http.get(o+"/ticket/listByProductId?productId="+t,e).then(function(t){n(t)}).catch(function(t){i(t)})})},e.ticketChange=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.post(o+"/ticket/change",t).then(function(t){e(t)}).catch(function(t){n(t)})})},e.ticketListByOrderNo=function(){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(t,e){$http.get(o+"/ticket/listByOrderNo/"+params).then(function(e){t(e)}).catch(function(t){e(t)})})},e.ticketRefund=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.post(o+"/ticket/refund",{tid:t}).then(function(t){e(t)}).catch(function(t){n(t)})})},e.orderCreate=function(t){return sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.post(o+"/order/save",t).then(function(t){e(t)}).catch(function(t){n(t)})})},e.orderDetail=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.get(o+"/order/details/"+t).then(function(t){e(t)}).catch(function(t){n(t)})})},e.orderCancel=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.post(o+"/order/cancel",{orderno:t}).then(function(t){e(t)}).catch(function(t){n(t)})})},e.orderPay=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.post(o+"/order/pay",{orderno:t}).then(function(t){e(t)}).catch(function(t){n(t)})})},e.orderPayCallback=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.post(o+"/v1/open/queryOrder",t).then(function(t){e(t)}).catch(function(t){n(t)})})},e.orderList=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.get(o+"/order/list/"+t).then(function(t){e(t)}).catch(function(t){n(t)})})},e.orderDelete=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.post(o+"/order/delete",{orderno:t}).then(function(t){e(t)}).catch(function(t){n(t)})})},e.listCanRefundOrChange=function(){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(t,e){$http.get(o+"/ticket/listCanRefundOrChange").then(function(e){t(e)}).catch(function(t){e(t)})})},e.ticketDetails=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.get(o+"/ticket/info/"+t).then(function(t){e(t)}).catch(function(t){n(t)})})},e.goPay=function(t){return window.sessionStorage.getItem("Authorization")&&n(window.sessionStorage.getItem("Authorization")),new Promise(function(e,n){$http.post(o+"/v1/open/pay",t).then(function(t){e(t)}).catch(function(t){n(t)})})}},19:function(t,e,n){n(165);var o=n(1)(n(92),n(274),"data-v-494c36e0",null);t.exports=o.exports},92:function(t,e,n){"use strict";function o(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var i=n(115),r=o(i);e.default=r.default},115:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=n(2);e.default={name:"createOrder",data:function(){return{orderName:"",orderAmount:""}},methods:{init:function(){this.resetData("all"),$util.setTitle("收银台")},resetData:function(t){switch(t){case"all":}},handleGoPay:function(){var t={orderNo:this.$route.query.orderNo,orderAmount:this.$route.query.orderAmount,payTypeEnum:"weChat",orderName:this.$route.query.orderName,sceneInfo:'{"h5_info": {"type":"Wap","wap_url": "chendeticket.zhengtoon.com","wap_name": "门票购买"}}',t:{spbillCreateIp:returnCitySN.cip}};(0,o.goPay)(t).then(function(t){0===t.meta.code&&(window.location.href=t.data.mwebUrl)}).catch(function(t){$util.toast({visible:!0,message:t.meta.message,position:"middle"})})}},mounted:function(){this.init(),this.orderName=this.$route.query.orderName,this.orderAmount=this.$route.query.orderAmount}}},165:function(t,e){},274:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"goPay-wrapper"},[n("div",{staticClass:"goPay-title"},[n("div",{staticClass:"border-bottom"},[n("span",{staticClass:"title"},[t._v("商品名称")]),t._v(" "),n("span",{staticClass:"ticket-type"},[t._v(t._s(t.orderName))])])]),t._v(" "),n("div",{staticClass:"pay-amount"},[n("span",{staticClass:"amount-title"},[t._v("支付金额")]),t._v(" "),n("span",{staticClass:"money"},[t._v(t._s(t.orderAmount)+"元")])]),t._v(" "),t._m(0),t._v(" "),n("div",{staticClass:"goPay",on:{click:t.handleGoPay}},[t._v("去支付")])])},staticRenderFns:[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"weixin-pay"},[n("div",{staticClass:"pay-left"},[n("i"),t._v(" "),n("div",{staticClass:"wei-title"},[n("p",[t._v("微信支付")]),t._v(" "),n("p",{staticStyle:{color:"#9C9C9C"}},[t._v("微信快捷支付")])])]),t._v(" "),n("div",{staticClass:"pay-right"})])}]}}});