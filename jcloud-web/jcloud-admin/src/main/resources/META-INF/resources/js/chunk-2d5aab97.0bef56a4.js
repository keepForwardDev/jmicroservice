(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d5aab97"],{"0f7b":function(t,a,e){"use strict";e.r(a);var r=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"formWrapper"},[e("el-form",{ref:"form",attrs:{rules:t.rules,model:t.formData,"label-width":"120px"}},[e("el-form-item",{attrs:{label:"任务名称",prop:"name"}},[e("el-input",{staticClass:"text-input",model:{value:t.formData.name,callback:function(a){t.$set(t.formData,"name",a)},expression:"formData.name"}})],1),e("el-form-item",{attrs:{label:"所属项目",prop:"projectDicId"}},[e("el-select",{staticClass:"text-input",attrs:{placeholder:"请选择所属项目"},model:{value:t.formData.projectDicId,callback:function(a){t.$set(t.formData,"projectDicId",a)},expression:"formData.projectDicId"}},t._l(t.projectList,(function(a){return e("el-option",{key:a.id,attrs:{label:a.name,value:a.value}},[t._v(t._s(a.name))])})),1)],1),e("el-form-item",{attrs:{label:"数据源名称",prop:"dataSourceInfoId"}},[e("el-select",{staticClass:"text-input",attrs:{placeholder:"请选择所属项目"},model:{value:t.formData.dataSourceInfoId,callback:function(a){t.$set(t.formData,"dataSourceInfoId",a)},expression:"formData.dataSourceInfoId"}},t._l(t.dataSourceList,(function(t){return e("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})})),1)],1),e("el-form-item",{attrs:{label:"是否调度"}},[e("el-radio-group",{model:{value:t.formData.isTask,callback:function(a){t.$set(t.formData,"isTask",a)},expression:"formData.isTask"}},[e("el-radio",{attrs:{label:1}},[t._v("是")]),e("el-radio",{attrs:{label:0}},[t._v("否")])],1)],1),e("el-form-item",{attrs:{label:"描述"}},[e("el-input",{staticClass:"text-input",attrs:{type:"textarea",autosize:{minRows:2,maxRows:4},placeholder:"请输入内容"},model:{value:t.formData.remark,callback:function(a){t.$set(t.formData,"remark",a)},expression:"formData.remark"}})],1),e("el-form-item",[e("el-button",{attrs:{type:"primary"},on:{click:t.nexStep}},[t._v("下一步")]),e("el-button",{on:{click:t.closeDialog}},[t._v("取消")])],1)],1)],1)},o=[],l={name:"taskInfo",props:{formData:{type:Object,default:function(){return{}}},projectList:{type:Array,default:function(){return[]}},dataSourceList:{type:Array,default:function(){return[]}}},data:function(){var t=this,a=function(a,e,r){t.formData.projectDicId&&t.formData.dataSourceInfoId?t.formData.id?r():t.$get("/datacenter/metadata/isTaskExists/"+t.formData.projectDicId+"/"+t.formData.dataSourceInfoId).then((function(t){t.data?r():r(new Error("当前数据源对应的项目已存在，不允许创建多个！"))})):r()};return{rules:{name:[{required:!0,message:"请输入任务名称",trigger:"blur"}],projectDicId:[{required:!0,validator:a,trigger:"change"}],dataSourceInfoId:[{required:!0,validator:a,trigger:"change"}]}}},created:function(){},methods:{nexStep:function(){var t=this;this.formData.fetchTableName=!0,this.$refs.form.validate((function(a){a&&t.$emit("nextStep")}))},closeDialog:function(){this.$emit("closeDialog")}}},n=l,i=(e("6682"),e("cba8")),s=Object(i["a"])(n,r,o,!1,null,"0bf03b24",null);a["default"]=s.exports},6682:function(t,a,e){"use strict";e("9dca")},"9dca":function(t,a,e){}}]);