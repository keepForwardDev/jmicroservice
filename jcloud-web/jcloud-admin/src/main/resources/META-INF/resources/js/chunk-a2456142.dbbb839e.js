(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-a2456142"],{1046:function(t,e,n){},"5eb4":function(t,e,n){"use strict";n("1046")},ea8e:function(t,e,n){"use strict";n.r(e);var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return t.easyFlowVisible?n("div",{staticStyle:{height:"calc(83vh)"}},[n("el-row",[n("el-col",{attrs:{span:24}},[n("div",{staticClass:"ef-tooltar"},[n("el-link",{attrs:{type:"primary",underline:!1}},[t._v(" "+t._s(t.data.name)+" ")]),n("el-divider",{attrs:{direction:"vertical"}}),n("el-button",{attrs:{type:"text",icon:"el-icon-delete",size:"large",disabled:!this.activeElement.type},on:{click:t.deleteElement}}),n("el-divider",{attrs:{direction:"vertical"}}),n("el-divider",{attrs:{direction:"vertical"}}),n("el-button",{attrs:{type:"text",icon:"el-icon-plus",size:"large"},on:{click:t.zoomAdd}}),n("el-divider",{attrs:{direction:"vertical"}}),n("el-button",{attrs:{type:"text",icon:"el-icon-minus",size:"large"},on:{click:t.zoomSub}}),n("div",{staticStyle:{float:"right","margin-right":"5px"}},[n("el-button",{attrs:{type:"info",plain:"",round:"",icon:"el-icon-back",size:"mini"},on:{click:t.lastStep}},[t._v(" 上一步 ")]),n("el-button",{attrs:{type:"primary",plain:"",round:"",icon:"el-icon-document",size:"mini"},on:{click:t.saveData}},[t._v(" 保存 ")])],1)],1)])],1),n("div",{staticStyle:{display:"flex",height:"calc(100vh - 180px)"}},[n("div",{staticClass:"wrap-menu-lists"},[n("el-scrollbar",{ref:"scrollbarMenu",staticClass:"menu-scrollbar"},[n("node-menu",{ref:"nodeMenu",attrs:{menuList:t.tableList},on:{addNode:t.addNode}})],1)],1),n("div",{directives:[{name:"flowDrag",rawName:"v-flowDrag"}],ref:"efContainer",staticClass:"container",attrs:{id:"efContainer"}},[t._l(t.data.nodeList,(function(e){return[n("flow-node",{key:e.id,attrs:{id:e.id,node:e,activeElement:t.activeElement},on:{changeNodeSite:t.changeNodeSite,nodeRightMenu:t.nodeRightMenu,clickNode:t.clickNode}})]})),n("div",{staticStyle:{position:"absolute",top:"2000px",left:"2000px"}},[t._v(" ")])],2),n("div",{staticStyle:{width:"300px","border-left":"1px solid #dce3e8","background-color":"#FBFBFB"}},[n("flow-node-form",{ref:"nodeForm",on:{setLineLabel:t.setLineLabel,repaintEverything:t.repaintEverything}})],1)]),t.flowInfoVisible?n("flow-info",{ref:"flowInfo",attrs:{data:t.data}}):t._e(),t.flowHelpVisible?n("flow-help",{ref:"flowHelp"}):t._e()],1):t._e()},o=[],a=(n("59d7"),n("a9b6"),n("3bdf"),n("c284"),n("28fd"),n("3e22"),n("16e1"),n("162b"),n("25d5")),s=n.n(a),l=(n("a778"),n("ab70")),r=n("c014"),c=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{ref:"tool",staticClass:"flow-menu"},t._l(t.menuList,(function(e){return n("div",{key:e.id},[n("span",{staticClass:"ef-node-pmenu",on:{click:function(n){return t.openMenu(e)}}},[n("i",{class:{"el-icon-caret-bottom":e.open,"el-icon-caret-right":!e.open}}),t._v(" "+t._s(e.name))]),n("ul",{directives:[{name:"show",rawName:"v-show",value:e.open,expression:"menu.open"}],staticClass:"ef-node-menu-ul"},[n("draggable",{attrs:{options:t.draggableOptions},on:{end:t.end,start:t.move},model:{value:e.children,callback:function(n){t.$set(e,"children",n)},expression:"menu.children"}},t._l(e.children,(function(e){return n("li",{key:e.id,staticClass:"ef-node-menu-li",attrs:{title:e.type,type:e.type}},[t._v(" "+t._s(e.name)+" ")])})),0)],1)])})),0)},d=[],u={left:-1,top:-1},f={props:{menuList:{type:Array,default:function(){return[]}}},data:function(){return{activeNames:"1",draggableOptions:{preventOnFilter:!1,sort:!1,disabled:!1,ghostClass:"tt",forceFallback:!0},defaultOpeneds:[],nodeMenu:{}}},components:{draggable:s.a},created:function(){this.isFirefox()&&(document.body.ondrop=function(t){u.left=t.layerX,u.top=t.clientY-50,t.preventDefault(),t.stopPropagation()})},methods:{getMenuByType:function(t){for(var e=0;e<this.menuList.length;e++)for(var n=this.menuList[e].children,i=0;i<n.length;i++)if(n[i].type===t)return n[i]},move:function(t,e,n,i){var o=t.item.attributes.type.nodeValue;this.nodeMenu=this.getMenuByType(o)},end:function(t,e){this.$emit("addNode",t,this.nodeMenu,u)},isFirefox:function(){var t=navigator.userAgent;return t.indexOf("Firefox")>-1},openMenu:function(t){t.open=!t.open,this.$forceUpdate()}}},m=f,h=n("cba8"),p=Object(h["a"])(m,c,d,!1,null,null,null),b=p.exports,v=n("1ce7"),g=n("a837"),y=n("f779"),w=n("7c98"),L=n.n(w),j={props:{dataList:{type:Array,default:function(){return[]}},formData:{type:Object,default:function(){return{}}}},computed:{tableList:function(){var t=this,e=this.dataList.map((function(e,n){return{id:n,type:e.tableName+"#"+t.formData.dataSourceInfoId,name:e.remarks?e.remarks:e.tableName,ico:"fa fa-arrows",style:{}}})),n={id:"1",type:"group",name:"表节点",ico:"el-icon-video-play",open:!0,children:e};return[n]}},data:function(){return{jsPlumb:null,easyFlowVisible:!0,flowInfoVisible:!1,loadEasyFlowFinish:!1,flowHelpVisible:!1,activeElement:{type:void 0,nodeId:void 0,sourceId:void 0,targetId:void 0},data:{nodeList:[],lineList:[]},zoom:.5}},mixins:[l["a"]],components:{draggable:s.a,flowNode:r["a"],nodeMenu:b,FlowInfo:v["a"],FlowNodeForm:y["a"],FlowHelp:g["a"]},directives:{flowDrag:{bind:function(t,e,n,i){e&&(t.onmousedown=function(e){if(2!=e.button){var n=e.clientX,i=e.clientY;t.style.cursor="move",document.onmousemove=function(e){e.preventDefault();var o=e.clientX-n;n=e.clientX,t.scrollLeft+=-o;var a=e.clientY-i;i=e.clientY,t.scrollTop+=-a},document.onmouseup=function(e){t.style.cursor="auto",document.onmousemove=null,document.onmouseup=null}}})}}},mounted:function(){this.jsPlumb=jsPlumb.getInstance(),this.getRelation()},methods:{getUUID:function(){return Math.random().toString(36).substr(3,10)},jsPlumbInit:function(){var t=this;this.jsPlumb.ready((function(){t.jsPlumb.importDefaults(t.jsplumbSetting),t.jsPlumb.setSuspendDrawing(!1,!0),t.loadEasyFlow(),t.jsPlumb.bind("click",(function(e,n){t.activeElement.type="line",t.activeElement.sourceId=e.sourceId,t.activeElement.targetId=e.targetId,t.$refs.nodeForm.lineInit({from:e.sourceId,to:e.targetId,label:e.getLabel()})})),t.jsPlumb.bind("connection",(function(e){var n=e.source.id,i=e.target.id;t.loadEasyFlowFinish&&t.data.lineList.push({from:n,to:i})})),t.jsPlumb.bind("connectionDetached",(function(e){t.deleteLine(e.sourceId,e.targetId)})),t.jsPlumb.bind("connectionMoved",(function(e){t.changeLine(e.originalSourceId,e.originalTargetId)})),t.jsPlumb.bind("contextmenu",(function(t){console.log("contextmenu",t)})),t.jsPlumb.bind("beforeDrop",(function(e){var n=e.sourceId,i=e.targetId;return n===i?(t.$message.error("节点不支持连接自己"),!1):t.hasLine(n,i)?(t.$message.error("该关系已存在,不允许重复创建"),!1):t.hashOppositeLine(n,i)?(t.$message.error("不支持两个节点之间连线回环"),!1):(t.$message.success("连接成功"),!0)})),t.jsPlumb.bind("beforeDetach",(function(t){console.log("beforeDetach",t)})),t.jsPlumb.setContainer(t.$refs.efContainer)}))},loadEasyFlow:function(){for(var t=0;t<this.data.nodeList.length;t++){var e=this.data.nodeList[t];this.jsPlumb.makeSource(e.id,L.a.merge(this.jsplumbSourceOptions,{})),this.jsPlumb.makeTarget(e.id,this.jsplumbTargetOptions),e.viewOnly||this.jsPlumb.draggable(e.id,{containment:"parent",stop:function(t){console.log("拖拽结束: ",t)}})}for(t=0;t<this.data.lineList.length;t++){var n=this.data.lineList[t],i={source:n.from,target:n.to,label:n.label?n.label:"",connector:n.connector?n.connector:"",anchors:n.anchors?n.anchors:void 0,paintStyle:n.paintStyle?n.paintStyle:void 0};this.jsPlumb.connect(i,this.jsplumbConnectOptions)}this.$nextTick((function(){this.loadEasyFlowFinish=!0}))},setLineLabel:function(t,e,n){var i=this.jsPlumb.getConnections({source:t,target:e})[0];n&&""!==n?i.addClass("flowLabel"):(i.removeClass("flowLabel"),i.addClass("emptyFlowLabel")),i.setLabel({label:n}),this.data.lineList.forEach((function(i){i.from==t&&i.to==e&&(i.label=n)}))},deleteElement:function(){var t=this;"node"===this.activeElement.type?this.deleteNode(this.activeElement.nodeId):"line"===this.activeElement.type&&this.$confirm("确定删除所点击的线吗?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){var e=t.jsPlumb.getConnections({source:t.activeElement.sourceId,target:t.activeElement.targetId})[0];t.jsPlumb.deleteConnection(e)})).catch((function(){}))},deleteLine:function(t,e){this.data.lineList=this.data.lineList.filter((function(n){return n.from!=t||n.to!=e})),this.activeElement.type="",this.activeElement.nodeId="",this.$refs.nodeForm.node={}},changeLine:function(t,e){this.deleteLine(t,e)},changeNodeSite:function(t){for(var e=0;e<this.data.nodeList.length;e++){var n=this.data.nodeList[e];n.id===t.nodeId&&(n.left=t.left,n.top=t.top)}},addNode:function(t,e,n){var i=t.originalEvent.clientX,o=t.originalEvent.clientY,a=this.$refs.efContainer,s=a.getBoundingClientRect(),l=i,r=o;if(l<s.x||l>s.width+s.x||r<s.y||s.y>s.y+s.height)this.$message.error("请把节点拖入到画布中");else{l=l-s.x+a.scrollLeft,r=r-s.y+a.scrollTop,l-=85,r-=16;var c=e.type,d=this.data.nodeList.filter((function(t){return t.type===e.type}));if(d.length>0)this.$message.error("该节点已存在画布中！");else{var u={id:c,name:e.name,type:c,left:l+"px",top:r+"px",ico:e.ico,state:"success"};this.data.nodeList.push(u),this.$nextTick((function(){this.jsPlumb.makeSource(c,this.jsplumbSourceOptions),this.jsPlumb.makeTarget(c,this.jsplumbTargetOptions),this.jsPlumb.draggable(c,{containment:"parent",stop:function(t){console.log("拖拽结束: ",t)}})}))}}},deleteNode:function(t){var e=this;return this.$confirm("确定要删除所选节点","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning",closeOnClickModal:!1}).then((function(){e.data.nodeList=e.data.nodeList.filter((function(e){return e.id!==t})),e.$nextTick((function(){this.jsPlumb.removeAllEndpoints(t)})),e.activeElement.type="",e.activeElement.nodeId="",e.$refs.nodeForm.node={}})).catch((function(){})),!0},clickNode:function(t){this.activeElement.type="node",this.activeElement.nodeId=t,this.$refs.nodeForm.nodeInit(this.data,t)},hasLine:function(t,e){for(var n=0;n<this.data.lineList.length;n++){var i=this.data.lineList[n];if(i.from===t&&i.to===e)return!0}return!1},hashOppositeLine:function(t,e){return this.hasLine(e,t)},nodeRightMenu:function(t,e){this.menu.show=!0,this.menu.curNodeId=t,this.menu.left=e.x+"px",this.menu.top=e.y+"px"},repaintEverything:function(){this.jsPlumb.repaint()},dataInfo:function(){this.flowInfoVisible=!0,this.$nextTick((function(){this.$refs.flowInfo.init()}))},dataReload:function(){var t=this;this.easyFlowVisible=!1,this.$nextTick((function(){t.easyFlowVisible=!0,t.$nextTick((function(){t.jsPlumb=jsPlumb.getInstance(),t.$nextTick((function(){t.jsPlumbInit()}))}))}))},zoomAdd:function(){this.zoom>=1||(this.zoom=this.zoom+.1,this.$refs.efContainer.style.transform="scale(".concat(this.zoom,")"),this.jsPlumb.setZoom(this.zoom))},zoomSub:function(){this.zoom<=0||(this.zoom=this.zoom-.1,this.$refs.efContainer.style.transform="scale(".concat(this.zoom,")"),this.jsPlumb.setZoom(this.zoom))},downloadData:function(){var t=this;this.$confirm("确定要下载该流程数据吗？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning",closeOnClickModal:!1}).then((function(){var e="data:text/json;charset=utf-8,"+encodeURIComponent(JSON.stringify(t.data,null,"\t")),n=document.createElement("a");n.setAttribute("href",e),n.setAttribute("download","data.json"),n.click(),n.remove(),t.$message.success("正在下载中,请稍后...")})).catch((function(){}))},openHelp:function(){this.flowHelpVisible=!0,this.$nextTick((function(){this.$refs.flowHelp.init()}))},lastStep:function(){this.$emit("lastStep")},saveData:function(){var t=this;this.formData.tableRelation=this.data,this.formData.tableInfo=this.dataList,this.$postJson("/datacenter/metadata/saveTaskInfo",this.formData).then((function(e){1===e.code&&(t.$message.success(e.msg),t.$emit("closeDialog"))}))},getRelation:function(){var t=this;this.formData.projectDicId&&this.$get("/datacenter/metadata/getEasyFlowTableRelation/"+this.formData.projectDicId).then((function(e){1===e.code&&(t.data.nodeList=e.data.nodeList,t.data.lineList=e.data.lineList,t.dataReload())}))}}},x=j,I=(n("5eb4"),Object(h["a"])(x,i,o,!1,null,null,null));e["default"]=I.exports}}]);