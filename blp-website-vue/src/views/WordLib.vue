<template>
  <!-- 这里是词汇库 -->
  <div class="total">
    <div class="header">
      <img :src="blpLogo" style="cursor:pointer" @click="toHome" />
      <div class="header-right">
        <div>
          <span @click="$router.push('websiteIndex')">论坛</span>
          <span @click="$router.push('businessTypeAppcase')">剧本案例</span>

          <span @click="$router.push('wordLib')"  style="color:purple"> 词汇库</span>
          <span @click="$router.push('sentenceLib')">句型库</span>
        </div>
        <div>
          <span @click="$router.push('talkWorkCenter')">创作中心</span>
          <a-avatar slot="avatar" :src="touxiang1" alt="Han Solo" />
        </div>
      </div>
    </div>
    <div class="middleBusBck">
      <div class="textContent1"><!-- 连接用户和功能 --></div>
      <div class="textContent2">
        <!-- 用户根据自己提出的业务需求编写剧本即可执行逻辑,无需编写代码即可完成功能的开发。 -->
      </div>
    </div>
    <div class="content">
      <div class="left"></div>
      <div class="right">

        <a-input-group class="inputGroup">
          <a-select default-value="编辑者" v-model="searchType">
            <a-select-option value="编辑者"> 编辑者 </a-select-option>
            <a-select-option value="固件分类"> 固件分类 </a-select-option>
          </a-select>
          <a-input-search
            placeholder="根据条件进行搜索"
            enter-button
            v-model="searchName"
            @search="onSearch"
          >
          </a-input-search>
         <span 
         style="margin-left:30px;background:white;border-radius:50%,"
         @click="refreshAllWord()"
         ><a-icon type="reload"  /></span>
        </a-input-group>
         
        
        <a-table
          :columns="columns"
          :row-key="(record) => record.id"
          :pagination="pagination"
          :loading="tableLoading"
          :data-source="wordData"
          bordered
         
        >
       <!--   @expand="handleExpandWord" 不在展示词汇绑定的工具信息，没有那个加号按钮了-->
         <span slot="unitCodeSlot" slot-scope="text,record">
        <span v-if="text">{{text.substring(0,40)}}</span>
 <a-dropdown v-if="text&&text.length>9">
    <a  @click="showUnitCode(record)">
      更多 <a-icon type="down" />
    </a>
    </a-dropdown> 
         </span>
         <span slot="customUpdateTimeTitle">
          修改时间
          <a-dropdown>
              <a-menu slot="overlay" @click="onDatePickClick" >
                
                <a-menu-item key="1"> 一个月内 </a-menu-item>
                <a-menu-item key="2"> 一周内</a-menu-item>
                <a-menu-item key="3"> 今天</a-menu-item>
              </a-menu>
              <a> <a-icon type="down" /> </a>
            </a-dropdown>
            </span>
          <span slot="firmwareType" slot-scope="text, index">
            
            <a-tag v-if="text == '1'" color="orange">吾托帮核心</a-tag>
            <a-tag v-else-if="text == '2'" color="green">普通工具</a-tag>
            <a-tag v-else-if="text == '3'" color="blue">系统工具</a-tag>
            <a-tag v-else-if="text == '4'" color="cyan">WebService及微服务</a-tag>
            <a-tag v-else-if="text == '0'" color="pink">值</a-tag>
            
          </span>
         <!--   <p slot="expandedRowRender" slot-scope="record" style="margin: 0">
      {{ record.description }}
    </p> -->
    <!-- slot-scope="firmwareParamInfo"   -->
   <!--   <a-descriptions 
     slot="expandedRowRender"
     slot-scope="record"
     title="词汇信息">
    <a-descriptions-item label="工具Id">
      {{record.firmwareParamInfo.toolId}}

    </a-descriptions-item>
    <a-descriptions-item label="工具名">
      {{record.firmwareParamInfo.toolName}}
    </a-descriptions-item>
    <a-descriptions-item label="固件id">
     {{record.firmwareParamInfo.id}}
    </a-descriptions-item>
    <a-descriptions-item label="固件名">
     {{record.firmwareParamInfo.firmwareName}}
    </a-descriptions-item>
   <a-descriptions-item label="固件语义" v-if="record.type==1">
  {{record.meaning}}  
    </a-descriptions-item>
  </a-descriptions> -->
        </a-table>
        <a-modal
      title="固件代码"
      :visible="firmwareCodeVisible"
      
      :footer="null"
      
      @cancel="handleFirmwareCodeCancel"
    >
      
     <!-- <p style="color:blue;font-weight:bold">{{firmwareCode}}</p> -->
     <p style="color:blue;font-weight:bold">{{meaning}}</p>
     
    </a-modal>
      </div>
    </div>

    <div class="footer">
      <span>020-62793083 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
      <span>xiaoxiao@scut.mail.edu.cn</span>
      <p>广州市华南理工大学B8实验室</p>
    </div>
  </div>
</template>

<script>
import ApplicationService from "@/service/application_service";
import DraftService from "@/service/draft_service";
import ChapterService from "@/service/chapter_service";

import blpLogo from "assets/blpLogo1.png";
import touxiang1 from "assets/touxiang1.jpg";
import WordService from "@/service/word_service";
import ParamService from "@/service/param_service";
import FirmwareService from "@/service/firmware_service";
const wordService = new WordService();
const firmwareService = new FirmwareService();
const paramService = new ParamService();
const applicationService = new ApplicationService();
const draftService = new DraftService();
const chapterService = new ChapterService();

const columns = [
  { title: "词汇名称", dataIndex: "name", key: "name", width: 80 },
  { title: "描述", dataIndex: "description", key: "description", width: 160 },

  //{ title: "工具", dataIndex: "toolName", key: "toolName",width:80 },
 /*  { title: "固件", dataIndex: "firmwareName", key: "firmwareName", width: 120 }, */
  { title: "固件代码", 
  dataIndex: "meaning",
   key: "meaning",
    width: 160 ,
   scopedSlots: { customRender: "unitCodeSlot" },
    },
  {
    title: "固件分类",
    dataIndex: "firmwareType",

    key: "firmwareType",
    width: 120,
    scopedSlots: { customRender: "firmwareType" },
  },
  { title: "编辑者", dataIndex: "creatorName", key: "creatorName", width: 80 },
  /*  {
    title: "状态",
    dataIndex: "status",
    key: "status",
    scopedSlots: { customRender: "statu" },
    width:160
  }, */
  {  slots:{title:"customUpdateTimeTitle"},
     dataIndex: "updateTime", key: "updateTime", width: 160 },
  /* {
    title: "操作",
    dataIndex: "",
    key: "x",
    scopedSlots: { customRender: "action" },
    width:160
  }, */
];
export default {
  data() {
    return {
      blpLogo,
      touxiang1,
      searchType:"编辑者",
      columns,
      wordData: [],
      //firmwareParamInfo:{},
      pagination: {
        page: 1,
        count: 10,
        total: 0,
      },
      tableLoading: false,
      addTeacherVisible: false,
      confirmLoading: false,
      labelCol: { span: 6 },
      wrapperCol: { span: 12 },

      whiteBckStyle: { background: "white" },
      greyBckStyle: { background: "#eaeaea" },
     firmwareCodeVisible :false,
     firmwareCode:'',
     meaning:'',
    };
  },
  created() {
    this.refreshAllWord();
  },
  watch: {},
  computed: {},

  methods: {
    toHome() {
      this.$router.push({ name: "Home" });
    },

    handleClick(e) {
      this.tabActiveKey = e.key;
    },
    refreshAllWord() {
      wordService.getAllPublicWordNoUnit({
        onSuccess: (model, fModel, json) => {
          console.log(json);
         // console.log("这是帮语网站中所有已绑定好固件的词汇");
          this.wordData = json.data.rows.word.sort((a, b) => {
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
      /*当读者观看词汇固件原代码这一栏时，由于代码长度太长，
    超出规定的限制后就展示“更多”字样，点击更多后会出现弹出一个弹窗，
    将如此多的代码显示出来*/
    showUnitCode(record){ 
      this.firmwareCodeVisible = true;
      //this.firmwareCode=record.firmwareCode;
      this.meaning=record.meaning;

    },
      handleFirmwareCodeCancel(){
       this.firmwareCodeVisible = false;
    },
    //展开词汇时获取与该词汇相关的固件及参数信息
    handleExpandWord(expanded, record){
firmwareService.getToolInfoFirmwareInfoParamInfoByWordId({
          wordId: record.id,
        },{
           onSuccess: (model, fModel, json) => {
            console.log(json.data.rows.firmware);
            //this.firmwareParamInfo=json.data.rows.firmware;
            this.$set(record, "firmwareParamInfo", json.data.rows.firmware);

            },
            onFail: (msg, json) => {
            console.log(msg, json);
          },

        });
    }, 
   
       onSearch(){

      if(this.searchType == '编辑者'){
        this.onSearchByCreatorName(this.searchName);
      }else {
        this.onSearchByUnitType(this.searchName)
      }
     
    },
    onSearchByCreatorName(value) {
      console.log(value);
      if(value==""){this.refreshAllWord();}
      else{
   wordService.getPublicWordByCreatorName(
        {
          creatorName: value,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.wordData = json.data.rows.word;
          },
          onFail: (msg, json) => {
            console.log(msg, json);wordData
          },
        }
      );
      }
   
    },
    onSearchByUnitType(value){
        console.log("根据固件种类查询词汇"+value);
      if(value==""){this.refreshAllWord();}
      else{
        let unitType=0;
        //值、工具、页面、WebService、微服务
          if(value=="值"){
        unitType=1;
      }else if(value=="普通工具"){
unitType=2;
      }else if(value=="系统工具"){
unitType=3;
      }else if(value=="WebService及微服务"){
unitType=4;
      }else{
        unitType=5;
      }
   wordService.getPublicWordByUnitType(

        {
          unitType: unitType,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.wordData = json.data.rows.word;
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      }
    },
    onDatePickClick({key}){
  console.log(`点击了选项 ${key}`);
   console.log(`${key}`);
   let datePickValue=`${key}`;

console.log('datePickValue是'+datePickValue);
   if(datePickValue=='2'){
wordService.getAllWordsInWeek({
   onSuccess: (model, fModel, json) => {
          console.log(json); //一个周内创建的词汇
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },

});
   }
    else if(datePickValue=='1'){
wordService.getAllWordsInMonth({
   onSuccess: (model, fModel, json) => {
          console.log(json); //一月内创建的词汇
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },

});
   }
    else if(datePickValue=='3'){
    wordService.getAllWordsInDay({
   onSuccess: (model, fModel, json) => {
          console.log(json); //今天一天内创建的词汇
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },

});

   }
    },
  },
};
</script>

<style lang="scss" scoped>
.ant-menu-horizontal > .ant-menu-submenu-selected {
  color: black;
  font-weight: bold;
  // border-bottom: 2px solid #eb2f96;
  border-bottom: none;
}
.ant-menu-horizontal > .ant-menu-submenu-selected:hover {
  color: black;
  font-weight: bold;
  // border-bottom: 2px solid #eb2f96;
  border-bottom: none;
}
.menuitemclass:hover {
  color: rgb(23, 22, 22);
  background: rgb(225, 225, 225);
}
.total {
  margin: 0 auto;
  background: rgb(234, 225, 225);
  min-width: 1100px;

  .header {
    height: 60px;
    display: flex;
    background: #fff;
    //justify-content:space-between;
    align-items: center;
    padding: 0 100px;
    img {
      width: 16%;
    }
     .header-right{
      width:100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  span{
      margin-left: 60px;
      font-weight: bold;
       cursor:pointer;
    }
     span:hover{
       margin-left: 60px;
      font-weight: bold;
    color: purple;
    }
  }
  
   }
    

  .middleBusBck {
    width: 100%;
    height: 340px;
    background: url("../assets/wordlib.png") no-repeat;
    background-position: center;
    background-size: 100% 100%;
    opacity: 0.8;
    padding-top: 120px;
    padding-left: 20px;
    margin-bottom:10px ;
    .textContent1 {
      color: black;
      font-size: 30px;
      font-style: oblique;
    }
    .textContent2 {
      color: black;
      margin-top: 10px;
    }
  }
  .content {
    display: flex;
   // border: 2px solid rgb(225, 225, 225);
    border-radius: 5px;
    margin-left: 20px;
    margin-right: 20px;

    .left {
      // background: #e8e9ea;
      background: white;

      border-radius: 5px;
    }
    .right {
      width: 100%;
      background: #fff;
      border-radius: 5px;
      
     
      .inputGroup{
        margin:0 auto;
display:flex;

width:50%;
justify-content: center;
align-items: center;
//border:2px solid black;

      }
      .ant-select{
        width:100px
      }
      

      
    
          

        }
      }
    
  
  .footer {
    // background: rgb(237, 170, 170);
    background: rgb(244, 242, 242);
    height: 120px;
    width: 100%;
    font-size: 15px;
    padding-top: 10px;
    text-align: center;
    color: black;
  }
}
</style>