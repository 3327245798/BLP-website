<template>
<!-- 2022年12月13日 -->
  <div class="total">
   <!--  <p style="color: black; font-weight: bold">
      您是句型开发者，请对用户提交的句型完善后上传
    </p> -->
    <a-button type="primary" @click="openAddSentenceModal()">
      上传句型
    </a-button>
    <a-table
      :columns="columns"
      :data-source="sentenceList"
      :row-key="(record) => record.id"
      :pagination="pagination"
      :loading="tableLoading"
      @expand="handleExpandSentence(expanded, record)"
      bordered
    >
      <span slot="tags" slot-scope="tags">
        <a-tag
          v-for="tag in tags"
          :key="tag"
          :color="
            tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'
          "
        >
          {{ tag.toUpperCase() }}
        </a-tag>
      </span>
      <span slot="action" slot-scope="text, record">
        <a-button
          type="primary"
          size="small"
          ghost
          @click="openModifySentenceModal(record)"
          >修改</a-button
        >
        <a-divider type="vertical" />
        <a-popconfirm
          cancelText="取消"
          okText="确定"
          title="确认删除?"
          @confirm="deleteSentence(record)"
        >
          <a-button type="danger" size="small" ghost>删除</a-button>
        </a-popconfirm>
      </span>
      <a-descriptions
        title="  上传的句型详细信息"
        layout="vertical"
        bordered
        slot="expandedRowRender"
        slot-scope="record"
      >
        <a-descriptions-item label="句型类型">
          {{ record.name ? record.name : "无" }}
        </a-descriptions-item>
        <a-descriptions-item label="创建者">
          {{ record.creatorName ? record.creatorName : "无" }}
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">
          {{ record.createTime ? record.createTime : "无" }}
        </a-descriptions-item>

        <a-descriptions-item label="句型" :span="3">
          <a-badge status="processing" />{{
            record.description ? record.description : "无"
          }}
        </a-descriptions-item>

        <a-descriptions-item label="句型实例" :span="3">
          {{ record.example ? record.example : "无" }}
        </a-descriptions-item>

        <a-descriptions-item label="固件" :span="3">
          {{ record.unitText ? record.unitText : "无" }}
        </a-descriptions-item>

        <a-descriptions-item label="固件描述" :span="3">
          {{ record.sentenceDescription ? record.sentenceDescription : "无" }}
        </a-descriptions-item>
      </a-descriptions>
    </a-table>
    <a-modal
      :title="sentenceFormTitle"
      :visible.sync="addSentenceVisible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
      ok-text="确认"
      cancel-text="取消"
      :width="900"
    >
   <div class="sentence-form-wrap">
     <div class="description" v-show="writeSentenceTagVisible">
      <a-icon  @click="onClose" type="close-circle" />
      <!-- <p v-for="(item,i) in  inputTextAreaList" :key="i">
          {{item.inputType}}&nbsp;&nbsp;的符号表示为&nbsp;&nbsp;{{item.character}}</p> -->
            <a-collapse v-model="activeKey">
      <a-collapse-panel :key="i"   v-for="(item,i) in inputTextAreaList" :header="item.character">
        <p>{{ item.inputType}}</p>
      </a-collapse-panel>
      
    </a-collapse>
    </div>

     <a-form-model
        id="components-form-demo-validate-other"
        ref="sentenceForm"
        :model="sentenceForm"
        v-bind="formItemLayout"
        :rules="sentenceRules"
      >
        <!-- <span>{{"您可以使用{}、[]、()、\<\>对参数进行标记，{语境输入域} - [日期输入域] - (文本输入域) -\<函数输入域\>"}}</span>
 -->
        <a-form-model-item prop="writeSentenceDesc" label="句型符号说明">
          <a-tag color="#f50" @click="showDiv">句型符号说明</a-tag>
        </a-form-model-item>
        <a-form-model-item
          prop="description"
          label="句型"
          class="writeSentence"
        >
          <a-textarea
            ref="sentencTextArea"
            v-model="sentenceForm.description"
            placeholder="请编写句型然后上传"
            :auto-size="{ minRows: 2, maxRows: 2 }"
          />
          <a-icon @click="showDiv" type="exclamation-circle" :style="{ 'font-size': '20px',color: 'blue','margin-left': '10px'}" 
        />
          <!--  <span>编写规则</span> -->
        </a-form-model-item>
        <a-form-model-item label="句型实例">
          <a-textarea
            ref="example"
            v-model="sentenceForm.example"
            placeholder="句型实例"
            :auto-size="{ minRows: 2, maxRows: 2 }"
          />
        </a-form-model-item>
        <a-form-model-item label="句型类型">
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
             <a-radio v-show="showCustomSentenceRadio" 
    :value="customSentenceType"
    > 
    {{customSentenceType}}
     </a-radio>
              <a-input
      v-if="inputSentenceTypeVisible==true"
      ref="input"
      type="text"
      size="small"
      :style="{ width: '78px' }"
      :value="inputValue"
      @change="handleInputChange"
      @blur="handleInputConfirm"
      @keyup.enter="handleInputConfirm"
    /> 
    <a-tag v-if="inputSentenceTypeVisible==false" style="background: #fff; borderStyle: dashed;font-size:14px" @click="showInput">
       <a-icon type="plus" /> 新建句型类型
        </a-tag>
        
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item label="固件" prop="unitText">
          <a-textarea
            ref="unitText"
            v-model="sentenceForm.unitText"
            placeholder="固件代码"
            :auto-size="{ minRows: 2, maxRows: 15 }"
          />
        </a-form-model-item>
        <a-form-model-item label="固件描述" prop="sentenceDescription">
          <a-textarea
            ref="sentenceDescription"
            v-model="sentenceForm.sentenceDescription"
            placeholder="句型的介绍、固件的执行过程概述、预期的执行结果"
            :auto-size="{ minRows: 2, maxRows: 6 }"
          />
        </a-form-model-item>
        <a-form-model-item label="创建者" >
         <span>{{userName}}</span>
        </a-form-model-item>
        <a-form-model-item label="创建时间" >
         <span>{{currentTime(new Date())}}</span>
        </a-form-model-item>
      </a-form-model>
   </div>
     
   
    </a-modal>
    <a-divider />
  <a-drawer
        title="句型符号说明"
        placement="bottom"
        closable="true"
        maskClosable="true"
        :visible="writeSentenceDrawerVisible"
        :after-visible-change="afterVisibleChange"
        @close="onDrawerClose"
      >
        <p v-for="(item,i) in  inputTextAreaList" :key="i">
          {{item.inputType}}&nbsp;&nbsp;的符号表示为&nbsp;&nbsp;{{item.character}}</p>

      

      </a-drawer> 
    <div class="sentence-editor-bottom"></div>
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
const userName= window.g_userName;

const columns = [
  {
    title: "句型类型",
    dataIndex: "name",
    key: "name",
    width: 70,
  },
  {
    title: "句型",
    dataIndex: "description",
    key: "description",
    // slots: { title: 'customTitle' },
    //scopedSlots: { customRender: 'name' },
    width: 120,
  },

  {
    title: "固件",
    dataIndex: "unitText",
    key: "unitText",
    width: 120,
    ellipsis: true,
  },

  {
    title: "句型描述及固件执行过程",

    key: "sentenceDescription",
    dataIndex: "sentenceDescription",
    width: 150,
  },
  {
    title: "创建者",
    key: "creatorName",
    dataIndex: "creatorName",
    //scopedSlots: { customRender: 'tags' },
    width: 80,
  },
  {
    title: "创建时间",
    key: "createTime",
    dataIndex: "createTime",
    //scopedSlots: { customRender: 'tags' },
    width: 100,
  },
  {
    title: "操作",
    key: "action",
    scopedSlots: { customRender: "action" },
    width: 100,
  },
];
const sentenceTemplate = JSON.stringify({
  name: "赋值型",
  toolId: "",
  unitAction: "",
  sequence: [],
  toolIdAndAction: "",
  serialization: "",
  example: "",
  unitText: "",
  sentenceDescription: "",
  description: "",
  sentenceFromUserId: 0,
  sentenceFromUserContent: "",
  textAreaustomSentence: "",
});

export default {
  components: {
    EditorContent,
  },
  data() {
    return {
      userName,
      columns,
      sentenceList: [],
      actionType: "ADD",
      sentenceFormTitle: "上传句型",
      addSentenceVisible: false,
      confirmLoading: false,
      writeSentenceTagVisible: false, //编写句型时的符号说明，点击tag标签显示
      writeSentenceDrawerVisible: false, //编写句型时的符号说明，会在添加句型或者修改句型时弹出来，遮罩在句型遮罩的上方
      //句型表单校验规则
      sentenceRules: {
        writeSentenceDesc: [
          {
            required: false,
            message: "请先查阅句型所用符号说明",
            trigger: "blur",
          },
        ],
        description: [
          { required: true, message: "请输入句型", trigger: "blur" },
          {pattern:new RegExp('[《\s\S》|<\s\S>|(\s\S)|{\s\S}]{1,}','g'),message:'必须包含句型符号[]、{}、()、<>等，请看句型符号说明'}, 
        ],
        unitText: [{ required: true, message: "请输入固件", trigger: "blur" }],
        sentenceDescription: [
          {
            required: true,
            message: "请输入句型描述及固件执行过程",
            trigger: "blur",
          },
        ],
      },
      inputSentenceTypeVisible: false,//控制新建句型类型输入框是否可见
      inputValue: '',//在输入框里输入的值
      customSentenceType:'我自定义的',//用户自定义句型类型
      showCustomSentenceRadio:false,//控制用户子自定义的单选按钮是否显示
      inputTextAreaList:[
        {inputType:"帮区域",character:"{}",example:"{帮区名称}"},
        {inputType:"部门域",character:"[]",example:"[部门名称]"},
        {inputType:"互动室域/消息板域",character:"()",example:""},
        {inputType:"岗位域",character:"<>",example:""},
        {inputType:"角色域",character:"##",example:""},
        {inputType:"工具域",character:"&&",example:""},
        {inputType:"部件域",character:"$$",example:""},
        {inputType:"日期域",character:"@@",example:""},
        {inputType:"操作符域",character:"<$",example:""},
        {inputType:"参数名域",character:"#$",example:""},
        {inputType:"any域",character:"$@",example:""},
        {inputType:"公式域",character:"#@",example:""},
      ],
      pagination: {
        page: 1,
        count: 10,
        total: 0,
        showTotal: (total) => `共 ${total} 条数据`,
        style: {
          position: "relative",
          right: "50%",
          transform: "translate(50%)",
        },
      },
      editor: null,
      tableLoading: false,
      formItemLayout: {
        labelCol: { span: 6 },
        wrapperCol: { span: 14 },
      },
      components: {},
      //开发者开发的句型信息
      sentenceForm: JSON.parse(sentenceTemplate),
    };
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "validate_other" });
  },
  created() {
    this.getAllSentenceInChecking();
    ///this.writeSentenceDrawerVisible=true;//句型符号说明
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
    //获取当前时间
    currentTime (date) {  
			var y = date.getFullYear();  
			var m = date.getMonth() + 1;  
			m = m < 10 ? ('0' + m) : m;  
			var d = date.getDate();  
			d = d < 10 ? ('0' + d) : d;  
			var h = date.getHours();  
			var minute = date.getMinutes();  
			minute = minute < 10 ? ('0' + minute) : minute;  
			return y + '-' + m + '-' + d + ' ' + h + ':' + minute;  
		},

    //新建句型类型
     showInput() {
      this.inputSentenceTypeVisible = true;
      this.$nextTick(function() {
        this.$refs.input.focus();
      });
    },
     handleInputChange(e) {
      this.inputValue = e.target.value;
      
    },
      handleInputConfirm() {
   
      console.log(this.inputValue); 
      this.customSentenceType=this.inputValue;

      this.showCustomSentenceRadio=true;//编写完自定义句型后，让单选按钮出现
       this.inputSentenceTypeVisible=false;
      this.inputValue= '';
     
      
     /*  Object.assign(this, {
        inputSentenceTypeVisible: false,
        inputValue: '', 
      });  */
     /*  let tags = this.tags;
      if (inputValue && tags.indexOf(inputValue) === -1) {
        tags = [...tags, inputValue];
      }
      console.log(tags);
      Object.assign(this, {
        tags,
        inputVisible: false,
        inputValue: '',
      }); */
    },
    //句型符号说明的抽屉
    afterVisibleChange(val) {
      console.log("编写句型时的符号说明", val);
    },
    showDiv() {
      //this.writeSentenceTagVisible = true;
      this.writeSentenceTagVisible = !this.writeSentenceTagVisible;
      
    },
    onClose() {
      this.writeSentenceTagVisible = false;
    },
    onDrawerClose(){
      console.log("关闭了句型符号说明抽屉");
      this.writeSentenceDrawerVisible=false;
    },
    handleExpandSentence(expanded, record) {
      console.log("展开了一行----");
      console.log(record);
    },
    openAddSentenceModal() {
      this.actionType = "ADD";
      this.addSentenceVisible = true;//打开添加句型弹窗
      this.sentenceFormTitle = "上传句型";
      this.sentenceForm = JSON.parse(sentenceTemplate);
      this.writeSentenceDrawerVisible=true;//句型符号说明，这个遮罩处用户编写句型弹窗之上，提醒用户先看符号说明在编写句型
    },
    openModifySentenceModal(sentence) {
      this.addSentenceVisible = true;//打开修改句型弹窗，和添加句型共用同一个弹窗
      this.actionType = "Modify";
      this.sentenceFormTitle = "修改句型";
      this.sentenceForm = sentence;
      this.writeSentenceDrawerVisible=true;//句型符号说明，这个遮罩处用户编写句型弹窗之上，提醒用户先看符号说明在编写句型
    },

    //获取所有审核中的句型
    getAllSentenceInChecking() {
      sentenceService.getAllSentenceByStatus(
        { status: 0 },
        {
          onSuccess: (model, fModel, json) => {
            console.log("获取处于审核中的句型");
            console.log(json);
            this.sentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            this.pagination.total = json.data.rows.sentence.length;
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },

    //句型弹窗点击确定执行的方法
    handleOk(e) {
      this.$refs.sentenceForm.validate((valid) => {
        if (!valid) {
          return false;
        }

         this.submitSentence();
      });
     
     // this.addSentenceVisible = false;
    
    },
    handleCancel(e) {
      console.log("点击了取消按钮");
      this.addSentenceVisible = false;
      
    },

    onSelectSentenceNameChange(e) {
      console.log("选中了", e.target.value);
    },

    submitSentence() {
      if (this.actionType == "ADD") {
        sentenceService.createSentence(this.sentenceForm, {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.currentSentenceDetail = json.data.rows;
            console.log(this.currentSentenceDetail);
            (this.sentenceForm = {
              name: "赋值型",
              toolId: "",
              unitAction: "",
              sequence: [],
              toolIdAndAction: "",
              serialization: "",
              example: "",
              unitText: "",
              sentenceDescription: "",

              sentenceFromUserId: 0,
              sentenceFromUserContent: "",
              textAreaustomSentence: "",
            }),
              this.$message.success("添加句型成功");
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        });
      } else {
        sentenceService.updateSentence(this.sentenceForm, {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.$message.success("修改句型成功");
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        });
      }
      this.getAllSentenceInChecking(); //删除或者添加后进行页面刷新
      this.addSentenceVisible = false;
      this.confirmLoading = false;
    },

    deleteSentence(sentence) {
      sentenceService.deleteSentence(sentence, {
        onSuccess: (model, fModel, json) => {
          console.log(json);
          this.$message.success("删除句型成功");
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
      });
      this.getAllSentenceInChecking(); //删除或者添加后进行页面刷新
    },

    tagChange(paramCname, paramEname) {
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
<style lang="scss" >
#components-badge-demo-dot .anticon-notification {
  width: 16px;
  height: 16px;
  line-height: 16px;
  font-size: 16px;
}
.total {
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
.writeSentence {
  display: flex;

  .ant-form-item-children{
    display: flex;
    flex-direction: row;
  }
}

.sentence-form-wrap{
  display: flex;

  .description{
    width: 200px;
  };
}
</style>