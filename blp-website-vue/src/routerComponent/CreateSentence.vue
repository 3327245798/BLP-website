<template>
  <div>
    <a-button class="a" type="primary" @click="submitAddSentence()">
      提交句型
    </a-button>
    <a-form
      id="components-form-demo-validate-other"
      :form="form"
      v-bind="formItemLayout"
      @submit="handleSubmit"
    >
      <a-form-item label="句型名称">
        <a-radio-group
          v-model="sentenceName"
          @change="onSelectSentenceNameChange"
          :default-value="赋值型"
        >
          <a-radio value="赋值型"> 赋值型 </a-radio>
          <a-radio value="命令型"> 命令型 </a-radio>
          <a-radio value="授权型"> 授权型 </a-radio>
          <a-radio value="消息型"> 消息型 </a-radio>
          <a-radio value="条件型"> 条件型 </a-radio>
          <a-radio value="循环型"> 循环型 </a-radio>
          <a-radio value="定时型"> 定时型 </a-radio>
          <a-radio value="监听型"> 监听型 </a-radio>
        </a-radio-group>
      </a-form-item>

      <!-- <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="工具" has-feedback>
            <a-select
              v-model="toolId"
              placeholder="请选择工具"
              @select="handleSelectTool"
            >
              <a-select-option
                v-for="t in tools"
                :key="t.tooId"
                :value="t.toolId"
              >
                {{ t.toolName }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="部件" has-feedback>
            <a-select
              v-model="unitAction"
              placeholder="请选择部件"
              @select="handleSelectUnitByToolIdAndAction"
            >
              <a-select-option
                v-for="u in unitsByToolId"
                :key="u.id"
                :value="u.actionApi"
              >
                {{ u.firmwareName }}-{{ u.actionApi }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row> -->

      <!-- <a-form-item label="参数">
        <span v-for="(param, index) in paramArr" :key="index" id="components-badge-demo-dot">
          
           <a-tag v-if="index == 0"  color="magenta" @click="tagChange(param.cnName,param.name)">{{
            param.cnName
          }}
         <a-badge dot v-if="param.required==1"> </a-badge>
          </a-tag> 
            <a-tag color="volcano"
             v-else-if="index == 1"
            @click="tagChange(param.cnName,param.name)"
            >{{param.cnName }}
            <a-badge dot  v-if="param.required==1">  </a-badge>   
            </a-tag>
          <a-tag
            v-else-if="index == 2"
            color="orange"
            @click="tagChange(param.cnName,param.name)"
            >{{ param.cnName}}
            <a-badge dot   v-if="param.required==1"/>
            </a-tag
          >
          <a-tag
            v-else-if="index == 3"
            color="gold"
            @click="tagChange(param.cnName,param.name)"
            >{{ param.cnName }}
            <a-badge dot  v-if="param.required==1" />
            </a-tag
          >
          <a-tag
            v-else-if="index == 4"
            color="lime"
            @click="tagChange(param.cnName,param.name)"
            >{{ param.cnName }}
            <a-badge dot  v-if="param.required==1" /></a-tag
          >
          
          <a-tag
            v-else-if="index == 5"
            color="blue"
            @click="tagChange(param.cnName,param.name)"
            >{{ param.cnName }}
            <a-badge dot v-if="param.required==1" /></a-tag
          >
          
          <a-tag
            v-else-if="index == 6"
            color="green"
            @click="tagChange(param.cnName,param.name)"
            >{{ param.cnName}}
            <a-badge dot v-if="param.required==1" />
            </a-tag
          >
          <a-tag
            v-else-if="index == 7"
            color="cyan"
            @click="tagChange(param.cnName,param.name)"
            >{{ param.cnName }}
            <a-badge dot v-if="param.required==1" />
            </a-tag
          >
          <a-tag
            v-else-if="index == 8"
            color="geekblue"
            @click="tagChange(param.cnName,param.name)"
            >{{ param.cnName }}
            <a-badge dot v-if="param.required==1" />
            </a-tag
          >
        </span>
      </a-form-item> -->
     <!--  <a-form-item label="参数标记">
       <span>{{"您可以使用{}、[]、()、\<\>对参数进行标记，{语境输入域} - [日期输入域] - (文本输入域) -\<函数输入域\>"}}</span>
     </a-form-item> -->
      <a-form-item label="开发句型">
        <a-textarea
          ref="sentencTextArea"
          v-model="textAreaustomSentence"
          placeholder="请编写句型"
          :auto-size="{ minRows: 2, maxRows:2 }"
        />
      </a-form-item>
       <a-form-item label="句型实例">
        <a-textarea
          ref="example"
          v-model="example"
          placeholder="句型实例"
          :auto-size="{ minRows: 2, maxRows: 2 }"  
        />
      </a-form-item>
       <a-form-item label="固件代码">
        <a-textarea
          ref="unitText"
          v-model="unitText"
          placeholder="固件"
          :auto-size="{ minRows: 2, maxRows: 12 }"
        />
      </a-form-item>
       <a-form-item label="固件描述">
        <a-textarea
          ref="sentenceDescription"
          v-model="sentenceDescription"
          placeholder="句型的介绍、固件的执行过程概述、预期的执行结果"
          :auto-size="{ minRows: 2, maxRows: 5 }"
        />
      </a-form-item>
      

      <!--  <a-form-item :wrapper-col="{ span: 12, offset: 6 }">
      <a-button type="primary" html-type="submit">
        Submit
      </a-button>
    </a-form-item> -->
    </a-form>

    <a-divider />

    <div class="sentence-editor-bottom">
      <a-descriptions
        title="  刚刚所创建的句型详细信息"
        layout="vertical"
        bordered
      >
        <a-descriptions-item label="句型名称">
          {{ currentSentenceDetail.name }}
        </a-descriptions-item>
        <a-descriptions-item label="创建者">
          {{ currentSentenceDetail.creatorName }}
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">
          {{ currentSentenceDetail.createTime }}
        </a-descriptions-item>

        <a-descriptions-item label="句型" :span="3">
          <a-badge status="processing" />{{ currentSentenceDetail.description }}
        </a-descriptions-item>
        
        <a-descriptions-item label="句型实例" :span="3">
          {{ currentSentenceDetail.example }}
        </a-descriptions-item>
        
        <a-descriptions-item label="固件代码" :span="3">
          {{ currentSentenceDetail.unitText }}
        </a-descriptions-item>
        
        <a-descriptions-item label="固件描述" :span="3">
         {{ currentSentenceDetail.sentenceDescription }}
        </a-descriptions-item>
      </a-descriptions>
    </div>
  </div>
</template>

<script>
import SentenceService from "@/service/sentence_service";

import BandService from "@/service/band_service";
import UnitService from "@/service/unit_service";
import FirmwareService from "@/service/firmware_service";

const sentenceService = new SentenceService();
const bandService = new BandService();
const unitService = new UnitService();
const firmwareService = new FirmwareService();


export default {
  data() {
    return {
      formItemLayout: {
        labelCol: { span: 6 },
        wrapperCol: { span: 14 },
      },
      tools: [], //对应工具下拉列表
      unitsByToolId: [], //对应部件下拉列表
      paramArr: [], //对应参数列表
      name: "SentencePage",
      components: {},

      sentenceName: "赋值型",
      toolId: "",
      unitAction: "",
      textAreaustomSentence: "",
      sequence: [],
      toolIdAndAction: "",
      serialization: "",
      example :"",
      unitText:"",
      sentenceDescription:"",


      pagination: {
        page: 1,
        count: 10,
        total: 0,
      },
      /* sentenceEntity:{
        description:'',
        name:'',
        type:'2',
      
      }, */
      currentSentenceDetail: {},
    };
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "validate_other" });
  },
  created() {
    this.getTools();
  },
  watch: {},
  computed: {},
  methods: {
    log(e) {
      console.log(e);
    },
    handleSubmit(a) {
      console.log(a);
    },
      onSelectSentenceNameChange(e) {
      console.log("选中了", e.target.value);
     
    },

    submitAddSentence() {
      sentenceService.createSentence(
        {
          name: this.sentenceName,
          /*根据梁子文的需求，页面上textarea的内容就是序列化表示的内容，可以直接作为serialization字段；
          有{}，[]，<>,()4中表示，再用正则将他们替换为《》，就可以作为description字段了*/
          description: this.textAreaustomSentence.replace(/\(|\{|\[|\</g, "《").replace(/\)|\}|\]|\>/g, "》"),
        
          sequence: this.sequence.join("$"),
          toolIdAndAction: this.toolIdAndAction,
          //serialization: this.textAreaustomSentence.replace(/\(.*?\)/g, "#"),
          serialization: this.textAreaustomSentence,

          type:2,
          example:this.example,
          unitText:this.unitText,
          sentenceDescription:this.sentenceDescription,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.currentSentenceDetail = json.data.rows;
            console.log(this.currentSentenceDetail);
            this.textAreaustomSentence = "";
            this.sentenceName = "";
            this.toolId = "";
            this.unitAction = "";
            this.example="",
            this.unitText="",
            this.sentenceDescription="",
            this.paramArr = null;
            this.$message.success("添加句型成功");
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    getTools() {
      bandService.getToolsInBand({
        onSuccess: (model, fModel, json) => {
          console.log("从核心获取所有的工具");
          console.log(json);
          this.tools = json.data.tools.rows.map((item) => {
            return {
              toolName: item.name,
              toolId: item.toolShopToolID,
              bandId: item.realObjID,
              bandName: item.bandName,
            };
          });
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
      });
    },
    handleSelectTool(value) {
      console.log(value);
      unitService.getUnitsByToolId(
        {
          toolId: value,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.unitsByToolId = json.data.rows.unitList;
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    handleSelectUnitByToolIdAndAction(value) {
      console.log(value);
      this.toolIdAndAction = this.toolId + "$" + value;
      firmwareService.getInputParamsByToolIdAndApiAction({
          toolId: this.toolId,
          apiAction: value,
      },{
     onSuccess: (model, fModel, json) => {
            console.log(json);
            this.paramArr=json.data.rows;
            
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
      });
    },
    tagChange(paramCname,paramEname) {
      this.textAreaustomSentence =
      this.textAreaustomSentence + "(" + paramCname + ")";
      //this.textAreaustomSentence  + paramCname ;
      this.sequence.push(paramEname);

      console.log(this.textAreaustomSentence);

       console.log(this.sequence);
      //console.log(this.textAreaustomSentence.replace(/\[.*?\]/g,'#'));

      this.$refs.sentencTextArea.focus();
    },
  },
};
</script>
<style lang="scss" scoped>

#components-badge-demo-dot .anticon-notification {
  width: 16px;
  height: 16px;
  line-height: 16px;
  font-size: 16px;
}
.sentence-editor-top {
  min-width: 1000px;
  margin-left: 50px;
  margin-right: 50px;

  .custom-input {
    border: none; /*去除所有边框*/
    outline: none; /*去除选中时出现的边框*/
    border-bottom-color: rgb(194, 103, 103); /*下边框颜色*/
    border-bottom-left-radius: 0px; /*下左边框的圆角为0px*/
    border-bottom-right-radius: 0px; /*下右边框的圆角为0px*/
    border-bottom-width: 0.2px; /*下边框的宽度*/
    border-bottom-style: solid; /*下边框的样式 solid-->实线*/
    width: 80px;
    color: #1d81e2;
    font-size: 12px;
  }
  .custom-input-span {
    font-weight: bold;
    margin-bottom: 10px;
  }
  .custom-input-tag {
    margin-bottom: 10px;
  }
}
.sentence-editor-bottom {
  padding: 0 30px;

  border: 1px solid rgb(244, 243, 243);
  border-radius: 5px;
}
</style>