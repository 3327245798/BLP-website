<template>

  <div class="total">
    <p style="color:black;font-weight:bold">您是句型开发者，请对用户提交的句型完善后上传</p>
     <a-table 
     :columns="commonUserSentenceColumns" 
     :data-source="sentenceFromCommonUser"
     :row-key="(record) => record.id"
     :pagination="pagination"
     :loading="tableLoading"
     @expand="handleExpandSentenceFromUser"
     bordered>
 
    <span slot="tags" slot-scope="tags">
      <a-tag
        v-for="tag in tags"
        :key="tag"
        :color="tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'"
      >
        {{ tag.toUpperCase() }}
      </a-tag>
    </span>
    <span slot="action" slot-scope="text, record">
      
      <a-button type="primary" size="small" ghost @click="showModal(record)">上传句型</a-button>
      <a-divider type="vertical" />
      <a-button type="primary" size="small" ghost>修改</a-button>
    </span>
    <a-descriptions
        title="  上传的句型详细信息"
        layout="vertical"
        bordered 
        slot="expandedRowRender" 
      >
      <!--  -->
        <a-descriptions-item label="句型名称">
          {{ developedSentenceInfo.name?developedSentenceInfo.name:'无' }}
        </a-descriptions-item>
        <a-descriptions-item label="创建者">
        {{ developedSentenceInfo.creatorName ?developedSentenceInfo.creatorName:'无' }}  
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">
         {{ developedSentenceInfo.createTime  ?developedSentenceInfo.createTime:'无'}}  
        </a-descriptions-item>

        <a-descriptions-item label="句型" :span="3">
          <a-badge status="processing" />{{developedSentenceInfo.description ?developedSentenceInfo.description:'无' }} 
        </a-descriptions-item>
        
        <a-descriptions-item label="句型实例" :span="3">
          {{ developedSentenceInfo.example ?developedSentenceInfo.example:'无'}}
        </a-descriptions-item>
        
        <a-descriptions-item label="固件" :span="3">
          {{ developedSentenceInfo.unitText?developedSentenceInfo.unitText: '无'}} 
        </a-descriptions-item>
        
        <a-descriptions-item label="固件描述" :span="3">
         {{ developedSentenceInfo.sentenceDescription?developedSentenceInfo.sentenceDescription:'无' }}
        </a-descriptions-item>
      </a-descriptions>
  </a-table>
   <a-modal
      title="上传句型"
      :visible.sync="DevSentenceVisible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
      ok-text="确认"
      cancel-text="取消"
    >
      <a-form
      id="components-form-demo-validate-other"
      :form="form"
      v-bind="formItemLayout"
      @submit="handleSubmit"
    >
    <a-form-item label="来自于用户的句型" >
       <p style="color:blue;font-weight:bold">{{sentenceForm.sentenceFromUserContent}}</p>
     </a-form-item>
      <a-form-item label="句型名称">
        <a-radio-group
          v-model="sentenceForm.name"
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

      <!-- <a-form-item label="参数标记">
       <span>{{"您可以使用{}、[]、()、\<\>对参数进行标记，{语境输入域} - [日期输入域] - (文本输入域) -\<函数输入域\>"}}</span>
     </a-form-item> -->
      <a-form-item label="上传句型">
         <a-textarea
          ref="sentencTextArea"
          v-model="textAreaustomSentence"
          placeholder="请编写句型然后上传"
          :auto-size="{ minRows: 2, maxRows: 2 }"
         
        /> 
    
      </a-form-item>
       <a-form-item label="句型实例">
        <a-textarea
          ref="example"
          v-model="sentenceForm.example"
          placeholder="句型实例"
          :auto-size="{ minRows: 2, maxRows: 2 }"  
        />
      </a-form-item>
       <a-form-item label="固件">
        <a-textarea
          ref="unitText"
          v-model="sentenceForm.unitText"
          placeholder="固件"
          :auto-size="{ minRows: 2, maxRows: 15 }"
        />
      </a-form-item>
       <a-form-item label="固件描述">
        <a-textarea
          ref="sentenceDescription"
          v-model="sentenceForm.sentenceDescription"
          placeholder="句型的介绍、固件的执行过程概述、预期的执行结果"
          :auto-size="{ minRows: 2, maxRows: 6 }"
        />
      </a-form-item>
      

      <!--  <a-form-item :wrapper-col="{ span: 12, offset: 6 }">
      <a-button type="primary" html-type="submit">
        Submit
      </a-button>
    </a-form-item> -->
    </a-form>
    </a-modal>

    <!-- <a-button class="a" type="primary" @click="submitAddSentence()">
      提交句型
    </a-button> -->
    

    <a-divider />

    <div class="sentence-editor-bottom">
      
    </div>
  </div>
</template>

<script>
import SentenceService from "@/service/sentence_service";
import SentenceFromUserService from "@/service/sentence_from_user_service";

import BandService from "@/service/band_service";
import UnitService from "@/service/unit_service";
import FirmwareService from "@/service/firmware_service";

const sentenceService = new SentenceService();
const sentenceFromUserService = new SentenceFromUserService();
const bandService = new BandService();
const unitService = new UnitService();
const firmwareService = new FirmwareService();

import { Editor, EditorContent } from "@tiptap/vue-2";
import StarterKit from "@tiptap/starter-kit";
import InputArea from "../InputArea";

const commonUserSentenceColumns = [
  {
    title:'普通用户创建的句型',
    dataIndex: 'sentenceFromUserContent',
    key: 'sentenceFromUserContent',
    slots: { title: 'customTitle' },
    scopedSlots: { customRender: 'name' },
    width:100,
  },
  {
    title: '用户期望的功能',
    dataIndex: 'hopeFunction',
    key: 'hopeFunction',
    width:150,
  },
  {
    title: '普通用户',
    dataIndex: 'commonUserName',
    key: 'commonUserName',
    width:50,
  },
  {
    title: '提交时间',
    key: 'createTime',
    dataIndex: 'createTime',
    //scopedSlots: { customRender: 'tags' },
    width:100,
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' },
    width:100,
  },
];


export default {
   components: {
    EditorContent,
  },
  data() {
    return {
      commonUserSentenceColumns,//普通用户创建的句型，每一列
      sentenceFromCommonUser:[],//普通用户创建的句型数据
       pagination: {
        page: 1,
        count: 10,
        total: 0,
      },
      editor:null,
       tableLoading: false,
      formItemLayout: {
        labelCol: { span: 6 },
        wrapperCol: { span: 14 },
      },
      tools: [], //对应工具下拉列表
      unitsByToolId: [], //对应部件下拉列表
      paramArr: [], //对应参数列表
      name: "SentencePage",
      components: {},
      //开发者开发的句型信息
  sentenceForm:{
      name: "赋值型",
      toolId: "",
      unitAction: "",
      sequence: [],
      toolIdAndAction: "",
      serialization: "",
      example :"",
      unitText:"",
      sentenceDescription:"",

      sentenceFromUserId:0,
      sentenceFromUserContent:"",
      textAreaustomSentence: "",

      },
       //开发者开发的句型的信息
      developedSentenceInfo:{
        name:"",
      creatorName:"",
      createTime:"",
      description:"",
      example:"",
      unitText:"",
      sentenceDescription:"",
      },
     

      pagination: {
        page: 1,
        count: 10,
        total: 0,
      },
      DevSentenceVisible:false,
      confirmLoading:false,

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
    //this.getTools();
    this.getCommonUserCreatedSentence();
  },
  watch: {},
    computed: {
    hasInputed() {
      return (item) => {
        if (this.editor.getText().indexOf(item) !== -1) {
          return "";
        } else {
          return "blue";
        }
      };
    },
  },
  methods: {
    log(e) {
      console.log(e);
    },
    getCommonUserCreatedSentence(){
sentenceFromUserService.getAllSentenceFromCommonUser({},{
  onSuccess: (model, fModel, json) => {
    console.log("获取普通用户创建的所有句型,等待开发者开发");
    console.log(json);
    this.sentenceFromCommonUser=json.data.rows.sentenceFromUser
    .sort((a, b) => {
                //根据词汇创建的先后顺序进行展示
                const date1 = new Date(a.createTime).getTime();
                const date2 = new Date(b.createTime).getTime();
                return date2 - date1;
              });

  },
  onFail: (msg, json) => {
            console.log(msg, json);
          },
});
    },
    handleExpandSentenceFromUser(expanded, record){
      console.log(expanded);
      console.log(record);
    this.getSentenceInfoByUserSentenceId(record.id);
    },
    getSentenceInfoByUserSentenceId(sentenceId){
sentenceService.getSentenceInfoByUserSentenceId({
  sentenceIdFromUser:sentenceId
},{
    onSuccess: (model, fModel, json) => {
          console.log(json);
          this.developedSentenceInfo=json.data.sentence;
        
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
});
    },
    //开发句型的弹窗
     showModal(record) {
      this.DevSentenceVisible = true;
      this.sentenceForm.sentenceFromUserId=record.id;
      this.sentenceForm.sentenceFromUserContent=record.sentenceFromUserContent;


    },
    //开发句型弹窗点击确定执行的方法
    handleOk(e) {
      

      this.submitAddSentence();

     this.DevSentenceVisible = false;
      
    },
    handleCancel(e) {
      console.log('点击了取消按钮');
      this.DevSentenceVisible = false;
    },

    handleSubmit(a) {
      console.log(a);
    },
      onSelectSentenceNameChange(e) {
      console.log("选中了", e.target.value);
     
    },

    submitAddSentence() {
      sentenceService.createSentence(
       /*  {
          name: this.sentenceName,
          //根据梁子文的需求，页面上textarea的内容就是序列化表示的内容，可以直接作为serialization字段；
          //有{}，[]，<>,()4中表示，再用正则将他们替换为《》，就可以作为description字段了
          description: this.textAreaustomSentence.replace(/\(|\{|\[|\</g, "《").replace(/\)|\}|\]|\>/g, "》"),
        
          sequence: this.sequence.join("$"),
          toolIdAndAction: this.toolIdAndAction,
          //serialization: this.textAreaustomSentence.replace(/\(.*?\)/g, "#"),
          serialization: this.textAreaustomSentence,

          type:2,
          example:this.example,
          unitText:this.unitText,
          sentenceDescription:this.sentenceDescription,
        }, */
        this.sentenceForm,
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.currentSentenceDetail = json.data.rows;
            console.log(this.currentSentenceDetail);
            /* this.textAreaustomSentence = "";
            this.sentenceName = "";
            this.toolId = "";
            this.unitAction = "";
            this.example="",
            this.unitText="",
            this.sentenceDescription="",
            this.paramArr = null; */
            this.sentenceForm= {
                name: "赋值型",
                toolId: "",
                unitAction: "",
                sequence: [],
                toolIdAndAction: "",
                serialization: "",
                example :"",
                unitText:"",
                sentenceDescription:"",

                sentenceFromUserId:0,
                sentenceFromUserContent:"",
                textAreaustomSentence: "",

                },
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
   mounted() {
    this.editor = new Editor({
      extensions: [StarterKit, InputArea],
    });
    this.editor.commands.insertContent(this.userScriptTemplate);
  },
  beforeDestroy() {
    this.editor.destroy();
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
.total{
  min-height: 750px;
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