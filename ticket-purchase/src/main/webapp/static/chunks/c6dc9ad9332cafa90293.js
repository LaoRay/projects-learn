webpackJsonp([6],{2:function(t,e){"use strict";function i(t){$http.defaults.headers.Authorization=t}Object.defineProperty(e,"__esModule",{value:!0});var n=window.CONFIG.DEV?window.CONFIG.API_DEV:window.CONFIG.API_HOST,o=function(t){window.alert(t)};$http.defaults.headers.sessionId=window.sessionStorage.getItem("sessionId");var s={SUCCESS:"0",TIMEOUT:"500",TOKEN_INVALID:"5001",CODE_INVALID:"5002"};$http.interceptors.response.use(function(t){var e=t.meta&&t.meta.code+"";switch(e){case s.SUCCESS:return t;case s.TOKEN_INVALID:case s.CODE_INVALID:return window.sessionStorage.removeItem("sessionId"),$http.defaults.headers.sessionId=void 0,o(t.meta.message||"当前会话已过期"),setTimeout(function(){window.location.href="toon://mwap/window?params="+JSON.stringify({functionType:1})},2e3),Promise.reject(t);default:return Promise.reject(t)}},function(t){return Promise.reject(t)});e.getSessionId=function(t,e,i){function o(t,i){return window.sessionStorage.setItem("Authorization",t),$http.defaults.headers.Authorization=t,e&&e(i)}return window.sessionStorage.getItem("Authorization")?o(window.sessionStorage.getItem("Authorization")):void $http.post(n+"/v1/open/getSessionId?code="+t).then(function(t){if(0===t.meta.code)return o(t.data.tokenType+";"+t.data.accessToken,t)}).catch(function(t){return i&&i(t)})},e.serverTime=function(){return sessionStorage.getItem("Authorization")&&i(sessionStorage.getItem("Authorization")),new Promise(function(t,e){$http.get(n+"/time/range").then(function(e){t(e)}).catch(function(t){e(t)})})},e.userInfo=function(){return sessionStorage.getItem("Authorization")&&i(sessionStorage.getItem("Authorization")),new Promise(function(t,e){$http.get(n+"/user/info").then(function(e){t(e)}).catch(function(t){e(t)})})},e.ticketList=function(t,e){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(i,o){$http.get(n+"/ticket/listByProductId?productId="+t,e).then(function(t){i(t)}).catch(function(t){o(t)})})},e.ticketChange=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.post(n+"/ticket/change",t).then(function(t){e(t)}).catch(function(t){i(t)})})},e.ticketListByOrderNo=function(){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(t,e){$http.get(n+"/ticket/listByOrderNo/"+params).then(function(e){t(e)}).catch(function(t){e(t)})})},e.ticketRefund=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.post(n+"/ticket/refund",{tid:t}).then(function(t){e(t)}).catch(function(t){i(t)})})},e.orderCreate=function(t){return sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.post(n+"/order/save",t).then(function(t){e(t)}).catch(function(t){i(t)})})},e.orderDetail=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.get(n+"/order/details/"+t).then(function(t){e(t)}).catch(function(t){i(t)})})},e.orderCancel=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.post(n+"/order/cancel",{orderno:t}).then(function(t){e(t)}).catch(function(t){i(t)})})},e.orderPay=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.post(n+"/order/pay",{orderno:t}).then(function(t){e(t)}).catch(function(t){i(t)})})},e.orderPayCallback=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.post(n+"/v1/open/queryOrder",t).then(function(t){e(t)}).catch(function(t){i(t)})})},e.orderList=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.get(n+"/order/list/"+t).then(function(t){e(t)}).catch(function(t){i(t)})})},e.orderDelete=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.post(n+"/order/delete",{orderno:t}).then(function(t){e(t)}).catch(function(t){i(t)})})},e.listCanRefundOrChange=function(){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(t,e){$http.get(n+"/ticket/listCanRefundOrChange").then(function(e){t(e)}).catch(function(t){e(t)})})},e.ticketDetails=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.get(n+"/ticket/info/"+t).then(function(t){e(t)}).catch(function(t){i(t)})})},e.goPay=function(t){return window.sessionStorage.getItem("Authorization")&&i(window.sessionStorage.getItem("Authorization")),new Promise(function(e,i){$http.post(n+"/v1/open/pay",t).then(function(t){e(t)}).catch(function(t){i(t)})})}},34:function(t,e,i){i(181);var n=i(1)(i(107),i(289),"data-v-96de6b44",null);t.exports=n.exports},107:function(t,e,i){"use strict";function n(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var o=i(131),s=n(o);e.default=s.default},131:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=i(2);e.default={name:"ticketType",data:function(){return{title:"门票购买",isActive:1,ticketList:{id:""},isToast:!1,allticketList:[],halfticketList:[]}},methods:{init:function(){this.resetData("all"),$util.setTitle("门票购买"),this.getTicketList()},resetData:function(t){switch(t){case"all":}},ticketTypeEvent:function(t){this.isActive=t},clickDetailsEvent:function(t){this.$router.push({name:"scenicSpotDetail",params:{id:t}})},getTicketList:function(){var t=this,e=this.$route.params.id;if(window.navigator&&window.navigator.onLine){var i={};(this.allticketList.length>0||this.halfticketList.length>0)&&(i={loading:!1}),(0,n.ticketList)(e,i).then(function(e){0===e.meta.code&&(t.allticketList=[],t.halfticketList=[],e.data.forEach(function(e,i){"全价票"!==e.ticketType&&"成人票"!==e.ticketType||t.allticketList.push(e),"半价票"!==e.ticketType&&"优惠票"!==e.ticketType||(t.halfticketList.push(e),t.halfticketList?t.isToast=!0:t.isToast=!1)}))}).catch(function(t){console.log(t),$util.toast({visible:!0,message:t.meta.message,position:"middle"})})}else $util.toast({visible:!0,message:"网络异常，请检查网络设置",position:"middle"})},toastShowevent:function(){this.isToast=!1}},beforeRouteEnter:function(t,e,i){i()},beforeRouteLeave:function(t,e,i){"scenicSpotDetail"!==e.name&&"loading"!==t.name&&"payResult"!==t.name||this.$router.push({name:"index"}),i()},activated:function(){this.init()},mounted:function(){}}},181:function(t,e){},289:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"ticket-type"},[i("div",{staticClass:"ticket-container"},[i("div",{staticClass:"ticket-tab border-bottom"},[i("div",{on:{click:function(e){t.ticketTypeEvent(1)}}},[i("button",{class:[1===t.isActive?"active":""]},[t._v("全价票")])]),t._v(" "),i("div",{on:{click:function(e){t.ticketTypeEvent(2)}}},[i("button",{class:[1!==t.isActive?"active":""]},[t._v("半价票")])])]),t._v(" "),i("div",{staticClass:"ticket-box"},[i("div",{directives:[{name:"show",rawName:"v-show",value:1===t.isActive,expression:"isActive === 1"}],staticClass:"ticket-all"},t._l(t.allticketList,function(e,n){return i("div",{key:n,staticClass:"ticket-item",class:[0===n?"first-item":""]},[i("h3",{attrs:{title:e.ticketName}},[t._v(t._s(e.ticketName))]),t._v(" "),i("div",{staticClass:"ticket-info"},[i("div",[i("p",[t._v(" 价格: "),i("span",{staticClass:"ticket-info-price"},[t._v(t._s(e.buyPrice?e.buyPrice.toFixed(2):0)+"元")])]),t._v(" "),i("p",{staticClass:"scenics-lsit"},[t._v("\n                "+t._s(e.summary?e.summary:e.ticketName)+"\n              ")])]),t._v(" "),i("span",{staticClass:"ticket-det",on:{click:function(i){t.clickDetailsEvent(e.id)}}},[t._v("详情")])])])})),t._v(" "),i("div",{directives:[{name:"show",rawName:"v-show",value:1!==t.isActive,expression:"isActive !== 1"}],staticClass:"ticket-half"},[i("div",{directives:[{name:"show",rawName:"v-show",value:t.isToast,expression:"isToast"}],staticClass:"toast-info"},[i("i",{staticClass:"icon icon-qiangdiao"}),t._v(" "),i("h3",[t._v("注：购买优惠票后，须在取票时向景区验票人员出示相关证件")]),t._v(" "),i("i",{staticClass:"icon icon-close",on:{click:t.toastShowevent}})]),t._v(" "),t._l(t.halfticketList,function(e,n){return i("div",{key:n,staticClass:"ticket-item",class:[0===n?"first-item":""]},[i("h3",[t._v(t._s(e.ticketName))]),t._v(" "),i("div",{staticClass:"ticket-info"},[i("div",[i("p",[t._v(" 价格: "),i("span",[t._v(t._s(e.buyPrice?e.buyPrice:0)+"元")])]),t._v(" "),i("p",{staticClass:"scenics-lsit"},[t._v("\n                "+t._s(e.summary?e.summary:e.ticketName)+"\n              ")])]),t._v(" "),i("span",{staticClass:"ticket-det",on:{click:function(i){t.clickDetailsEvent(e.id)}}},[t._v("详情")])])])})],2)])])])},staticRenderFns:[]}}});