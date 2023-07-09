<template>
  <div class="total">
    <div class="header">
      <img :src="blpLogo" style="cursor: pointer" @click="toHome" />
      <div class="header-right">
        <div class="nav">
          <span @click="gotowebsiteIndex()">论坛</span>
          <a-menu
            :default-selected-keys="['1']"
            mode="horizontal"
            @click="handleClick"
          >
            <a-sub-menu key="1">
              <span slot="title"
                ><span style="color: purple">剧本案例</span></span
              >
              <!--  <a-menu-item
                class="menuitemclass"
                v-for="item in businessList"
                :key="item.index"
              >
                {{ item.busnessType }}
              </a-menu-item> -->
            </a-sub-menu>
          </a-menu>
          <span @click="gotoWordLib()"> 词汇库</span>
          <span @click="gotosentenceLib()">句型库</span>
        </div>
        <div>
          <span @click="gototalkWorkCenter()">创作中心</span>
          <a-avatar slot="avatar" :src="touxiang1" alt="Han Solo" />
        </div>
      </div>
    </div>
    <div class="middleBusBck">
      <div class="textContent1">连接用户和功能</div>
      <div class="textContent2">
        用户根据自己提出的业务需求编写剧本即可执行逻辑,无需编写代码即可完成功能的开发。
      </div>
    </div>
    <div class="content">
      <div class="left"></div>
      <div class="right">
        <div class="rightContent">
          <!--    根据draftid获取草稿剧本list和分析剧本list -->
          <a-table
            :rowKey="
              (index, record) => {
                return index;
              }
            "
            :columns="columns"
            :data-source="newData"
            bordered
            style="width: 100%"
          >
            <a
              slot="name"
              slot-scope="text, record"
              @click="enterDraftList(record.draft)"
              >{{ text }}</a
            >

            <span slot="customTitle"><a-icon type="smile-o" /> 剧本名称</span>

            <!-- 草稿剧本 -->
            <a-list
              item-layout="horizontal"
              slot="draftChapterList"
              slot-scope="text, record"
              :data-source="record.draftChapterList"
            >
              <a-list-item slot="renderItem" slot-scope="item, index">
                <p
                  :style="
                    currentIndex === record.draft.id + '#' + item.id
                      ? draftChapterListStyle
                      : null
                  "
                  @mouseenter="styleChange(record.draft.id, item)"
                  @mouseleave="styleLeave"
                >
                  {{ item.content }}
                </p>
              </a-list-item>
            </a-list>
            <!--  分析剧本 -->
            <a-collapse slot="analyseChapterList" slot-scope="text, record">
              <a-collapse-panel
                v-for="(item, index) in record.analyseList"
                :key="index"
                :header="item.content"
                v-show="item.content != null"
                :style="
                  currentIndex === record.draft.id + '#' + item.draftChapterId
                    ? draftChapterListStyle
                    : null
                "
              >
                <CopyCode :code="item.unitCode"></CopyCode>
                <!-- 自定义组件 -->
                <!-- <MyComponent name="张三" v-bind:age="15" v-bind:stu="school"></MyComponent> -->
              </a-collapse-panel>
            </a-collapse>

            <span slot="action" slot-scope="text, record">
              <!-- <a-button type="primary" @click="getFirmwareCode($event, record)">查看固件代码</a-button> -->
              <!-- <a>删除</a> --><!-- <a-button type="primary">  删除</a-button> -->
              <a-button type="primary" class="ant-dropdown-link">
                部署</a-button
              >
              <a-button
                type="primary"
                class="ant-dropdown-link"
                @click="showDraftEnv(record)"
              >
                剧本执行要求</a-button
              >
            </span>
          </a-table>
        </div>
      </div>
      <a-modal
        v-model="DraftEnvVisible"
        title="剧本执行要求"
        @ok="handleOk"
        :footer="null"
      >
        <p style="font-weight: bold; font-size: 16px">
          剧本执行所需要的句型、词汇及工具,如下：
        </p>

        <p style="font-weight: bold; font-size: 16px">句型：</p>
        <div
          style="
            background-color: #fffff0;
            border-radius: 8px;
            padding: 4px 8px;
          "
        >
          <p v-for="(item, index) in draftEnv.sentenceTemplate" :key="index">
            {{ index + 1 }}.{{ item }}
          </p>
        </div>
        <!--  <p v-if="draftEnv.sentenceTemplate.length==0">无句型</p> -->
        <p style="font-weight: bold; font-size: 16px">词汇：</p>
        <div>
          <a-tag
            color="orange"
            v-for="(item, index) in draftEnv.word"
            :key="index"
            >{{ item }}</a-tag
          >
        </div>
        <!--   <p v-if="draftEnv.word.length==0">无词汇</p> -->
        <p style="font-weight: bold; font-size: 16px">工具：</p>
        <div>
          <a-tag
            color="geekblue"
            v-for="(item, index) in draftEnv.tool"
            :key="index"
            >{{ item }}</a-tag
          >
        </div>

        <!-- <p v-if="draftEnv.tool.length==0" >无工具</p> -->
      </a-modal>
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
import SentenceService from "@/service/sentence_service";
import blpLogo from "assets/blpLogo1.png";
import touxiang1 from "assets/touxiang1.jpg";
import Img0 from "assets/randomImg/0.gif";
import Img1 from "assets/randomImg/1.gif";
import Img2 from "assets/randomImg/2.gif";
import Img3 from "assets/randomImg/3.gif";
import Img4 from "assets/randomImg/4.gif";
import Img5 from "assets/randomImg/5.gif";
import Img6 from "assets/randomImg/6.gif";
import Img7 from "assets/randomImg/7.gif";
import Img8 from "assets/randomImg/8.gif";
import Img9 from "assets/randomImg/9.gif";
import { log } from "../common/env";
//import "highlight.js/styles/stackoverflow-dark.css"; // 可以切换其它样式风格，例如黑色主题
//import "highlight.js/lib/common";
//import hljsVuePlugin from "@/vue-plugin";
import CopyCode from "../myComponent/CopyCode.vue";
import { getBandId } from "@/common/env";

//import businessbackgroud from "assets/business.png";

const applicationService = new ApplicationService();
const draftService = new DraftService();
const chapterService = new ChapterService();
const sentenceService = new SentenceService();

export default {
  data() {
    const businessList = [
      { index: "1", busnessType: "物流业务" },
      { index: "2", busnessType: "服务操作系统业务" },
      { index: "3", busnessType: "管理型业务" },
      { index: "4", busnessType: "学习型业务" },
      { index: "5", busnessType: "计算型业务" },
      { index: "6", busnessType: "活动型业务" },
      { index: "7", busnessType: "其他业务" },
    ];
    const columns = [
      {
        dataIndex: "draft.name",
        key: "draft.name",
        slots: { title: "customTitle" },
        scopedSlots: { customRender: "name" },
        width: "10%",
      },
      {
        title: "草稿剧本",
        dataIndex: "draftChapterList",
        key: "draftChapterList",
        //slots: { title: "customTitle" },
        scopedSlots: { customRender: "draftChapterList" },
        width: "35%",
      },
      {
        title: "分析剧本",
        dataIndex: "analyseList.content",
        key: "analyseList",
        scopedSlots: { customRender: "analyseChapterList" },
        width: "35%",
      },
      {
        title: "操作",
        key: "action",
        scopedSlots: { customRender: "action" },
        width: "20%",
      },
    ];

    const data = [
      //本来是data
      {
        key: "1",
        draft: { name: "互帮互助型协作学习", creatorName: "梁子文" },
        draftChapterList: [
          { content: "xx引导者分析学习任务，分解学习任务，制定任务分解方案" },
          { content: "今天天气真好的草稿剧本" },
          { content: "打开开关灯工具" },
        ],
        analyseList: [
          { content: "执行学习工具的学习套件" },
          { content: "执行天气的查询部件，获取今天天气" },
          { content: "执行开关灯工具的开灯部件" },
        ],
      },
      {
        key: "2",
        draft: { name: "互查互评型协作学习", creatorName: "梁子文" },
        draftChapterList: [
          { content: "xx引导者分析学习任务，分解学习任务，制定任务分解方案" },
          { content: "今天天气真好的草稿剧本" },
          { content: "打开开关灯工具" },
        ],
        analyseList: [
          { content: "执行学习工具的学习套件" },
          { content: "执行天气的查询部件，获取今天天气" },
          { content: "执行开关灯工具的开灯部件" },
        ],
      },
      {
        key: "3",
        draft: { name: "番茄学习法", creatorName: "梁子文" },
        draftChapterList: [
          { content: "xx引导者分析学习任务，分解学习任务，制定任务分解方案" },
          { content: "今天天气真好的草稿剧本" },
          { content: "打开开关灯工具" },
        ],
        analyseList: [
          { content: "执行学习工具的学习套件" },
          { content: "执行天气的查询部件，获取今天天气" },
          { content: "执行开关灯工具的开灯部件" },
        ],
      },
    ];

    return {
      school: {
        No: 123123,
        schoolName: "湖北大学",
        majorName: "软件工程",
      },
      newData: data, //与data相比，不包含空值了

      columns,
      blpLogo,
      touxiang1,
      // businessbackgroud,
      businessList,

      tabActiveKey: "1",

      str: "",
      Img0,
      Img1,
      Img2,
      Img3,
      Img4,
      Img5,
      Img6,
      Img7,
      Img8,
      Img9,
      whiteBckStyle: { background: "white" },
      greyBckStyle: { background: "#eaeaea" },
      draft: [], //剧本列表（包括剧本本身，分析剧本和草稿剧本）
      draftChapterListStyle: { background: "yellow", color: "red" }, //当鼠标移动到草稿剧本的某句话是，该句话被高亮，同时与之相关的分析剧本对应的语句也要被高亮

      currentIndex: "0#0",
      unitText: "",
      DraftEnvVisible: false, //点击剧本执行环境按钮弹出对话框
      draftEnv: {
        sentenceTemplate: [],
        word: [],
        tool: [],
      },
    };
  },
  components: {
    //highlightjs: hljsVuePlugin.component,
    CopyCode,
    //MyComponent,
  },
  created() {
    this.getDraftList();
    //this.callback(1);
  },
  watch: {},
  computed: {},

  methods: {
    enterDraftList(record) {
      const url = `https://www.wetoband.com/tre//runSystemTool?gid=${getBandId()}&toolID=4389353&param={"draftId":${
        record.id
      },"applicationCaseId":${
        record.applicationCaseId
      },"draftVersion":1,"bandID":"${getBandId()}"}`;
      window.open(encodeURI(url));
    },
    //词汇库
    gotoWordLib() {
      let text = this.$router.resolve({
        path: "/wordLib",
      });
      window.open(text.href, "_blank");
    },
    //句型库
    gotosentenceLib() {
      let text = this.$router.resolve({
        path: "/sentenceLib",
      });
      window.open(text.href, "_blank");
    },
    //创作中心
    gototalkWorkCenter() {
      let text = this.$router.resolve({
        path: "/talkWorkCenter",
      });
      window.open(text.href, "_blank");
    },
    //首页
    gotowebsiteIndex() {
      let text = this.$router.resolve({
        path: "/websiteIndex",
      });
      window.open(text.href, "_blank");
    },
    toHome() {
      this.$router.push({ name: "Home" });
    },
    img() {
      return eval("this.Img" + Math.round(Math.random() * 9));
    },
    handleClick(e) {
      this.tabActiveKey = e.key;
    },
    draftClick(expanded, record) {
      //这个id 是剧本id，根据剧本id找该剧本下的章节list，
      console.log("出发了点击事件", record);
      // this.draftChapterVisible = true;
      chapterService.getDraftChapterListByDraftId(
        {
          draftId: record.id,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log("根据剧本id找该剧本下的章节list");
            console.log(json);
            //当点击展开剧本加号按钮时，获取该剧本下的所有章节

            record.draftChapteData = [...json.data.rows];
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },

    handleChapterCancel(e) {
      console.log("点击了叉");
      this.draftChapterVisible = false;
    },
    callback(e) {
      this.tabActiveKey = e;
      console.log(e);
      applicationService.getAppcaseBybusTpyeId(
        { businessTypeId: e },
        {
          onSuccess: (model, fModel, json) => {
            console.log("根据业务id找应用案例");
            console.log(json);
            this.appcaseList = [];
            json.data.rows.forEach((item) => {
              draftService.getDraftListByAppcaseId(
                { appcaseId: item.id },
                {
                  onSuccess: (model, fModel, json) => {
                    console.log("根据应用案例id找对应的所有剧本");
                    console.log(json);
                    let draftList = [];
                    draftList = json.data.rows.map((row) => {
                      let newDraft = { ...row };
                      newDraft.draftChapteData = [];
                      return newDraft;
                    });
                    item.draftList = draftList;
                    this.appcaseList.push(item);
                    console.log(draftList);
                  },
                  onFail: (msg, json) => {
                    console.log(msg, json);
                  },
                }
              );
              console.log(1111111);
              return item;
            });
            console.log(this.appcaseList);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    showDraftChapter(record) {
      console.log(record);
    },
    getDraftList() {
      draftService.getAllDraft(
        {},
        {
          onSuccess: (model, fModel, json) => {
            console.log(
              "从后台获取到的数据（剧本空壳子信息，草稿剧本及分析剧本）",
              json
            );
            console.log("获取所有的剧本,包含草稿剧本列表和分析剧本列表");

            this.data = json.data.rows;

            this.data.forEach((item) => {
              item.analyseList.forEach((analyse) => {
                let that = this;
                if (that.isJSON(analyse.serialization)) {
                  //分析剧本的这一句话
                  // console.log("进来进来进来")
                  let obj = JSON.parse(analyse.serialization);
                  let contentArr = [];
                  let tool = [];
                  let word = [];
                  if (Array.isArray(obj)) {
                    //兼容性处理，使用if语句处理（编辑器老版本插入的剧本）-此时serialization存放的数组格式
                    //表示每一句分析剧本，由给定域和输入域组成
                    obj.forEach((item) => {
                      //item的content也是一个数组，
                      let result = "";
                      try {
                        item.content.forEach((node) => {
                          if (node.type === "stem") {
                            result += node.name;
                          } else if (node.type === "field") {
                            if (typeof node.content == "object") {
                              result += node.content.name;
                            } else {
                              result += node.content;
                              word.push(node.content);
                            }
                          }
                        });
                      } catch (error) {
                        console.log(error.message);
                      }

                      contentArr.push(result);
                      let template = item.template;
                      // console.log("句型模板含不含工具",template);//句型模板
                      // console.log("template的类型",typeof template );
                      if (
                        template != null &&
                        Object.hasOwnProperty.call(template, "tool")
                      ) {
                        tool.push(template.tool);
                      }
                    });
                    //获取结果数组中的第一条数据，实际上只有一条，
                    //结构虽然是数组，本来应该是对象的格式，但是后盾不想改代码，所以这样做了，对于前段解析确实麻烦了点
                    analyse.content = contentArr[0];
                    analyse.tool = [...tool];
                    analyse.word = [...word];
                  } else {
                    //使用else语句处理新版本编辑器，此时analyse.serialization是对象,let obj = JSON.parse(analyse.serialization);
                    //ibj的属性有template（获取工具信息）、content（获取词汇，拼接句子）
                    //这里处理工具
                    let template = obj.template;
                    if (
                      template != null &&
                      Object.hasOwnProperty.call(template, "tool")
                    ) {
                      tool.push(template.tool);
                    }
                    //这里处理词汇及拼接句子
                    let analyseContentArr = obj.content;
                    console.log(
                      "json对象content属性的类型",
                      typeof analyseContentArr
                    );
                    if (Array.isArray(analyseContentArr)) {
                      let result1 = "";
                      analyseContentArr.forEach((node) => {
                        if (node.type === "stem") result1 += node.name;
                        else if (node.type === "field") {
                          if (typeof node.content == "object") {
                            result1 += node.content.name;
                          } else {
                            result1 += node.content;
                            word.push(node.content);
                          }
                        }
                      });
                      analyse.content = result1;
                    }
                    analyse.word = [...word];
                    analyse.tool = [...tool];
                  }
                }

                let sentenceId = analyse.sentenceId;
                sentenceService.getSentenceById(
                  { sentenceId },
                  {
                    onSuccess: (model, fModel, json) => {
                      //  console.log("每一个分析语句对应的句型固件代码",json);
                      analyse.unitCode = json.data.rows.unitText || "";
                      analyse.sentenceTemplate =
                        json.data.rows.description || "";
                    },
                    onFail: (msg, json) => {
                      console.log(msg, json);
                    },
                  }
                );
                return item.analyseList;
              });
              item.draftChapterList.forEach((draftChapter) => {
                let result = "";
                if (this.isJSON(draftChapter.serialization)) {
                  let draftChapterObj = JSON.parse(draftChapter.serialization);
                  let contentArr = draftChapterObj.content;
                  if (Array.isArray(contentArr)) {
                    contentArr.forEach((node) => {
                      if (node.type === "stem") result += node.name;
                      else if (node.type === "field") {
                        if (typeof node.content == "object") {
                          result += node.content.name;
                        } else {
                          result += node.content;
                        }
                      }
                    });
                  } else if (typeof contentArr == "string") {
                    result = contentArr;
                  }
                } else {
                  result = draftChapter.serialization;
                }
                draftChapter.content = result;

                return item.draftChapterList;
              });
            });
            this.newData = this.data.filter(
              (item) =>
                item.analyseList.length > 0 && item.draftChapterList.length > 0
            );
            console.log("newData----------------", this.newData); //newData是过滤空值后的数组
            console.log("----------------", this.data); //data是经处理过的数组，但是会有空值
            console.log(json);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    // json格式判断
    isJSON(str) {
      if (typeof str === "string") {
        try {
          var obj = JSON.parse(str);
          if (typeof obj == "object" && obj) {
            return true;
          } else {
            return false;
          }
        } catch {
          //  console.error('error!!')
          return false;
        }
      } else {
        console.log("不是一个字符串！");
      }
    },
    showDraftEnv(record) {
      this.DraftEnvVisible = true;
      console.log("该分析剧本对应的剧本执行环境", record);
      let toolArr = [];
      let wordArr = [];
      let sentenceArr = [];
      record.analyseList.forEach((item) => {
        if (item.sentenceTemplate) {
          sentenceArr.push(item.sentenceTemplate);
        }

        if (item.tool && item.tool.length > 0) {
          toolArr.push(...item.tool); //但是会存在空值，或者重复值【"计算型套件","计算型套件","帮区基础服务工具"，"","消息板工具"，undefined】
        }
        if (item.word && item.word.length > 0) {
          wordArr.push(...item.word); //但是会存在空字符串，或者重复值
        }
      });

      this.draftEnv.tool = this.uniqueArr(toolArr);
      this.draftEnv.word = this.uniqueArr(wordArr);
      this.draftEnv.sentenceTemplate = sentenceArr;
      console.log("这个分析剧本包含的工具", this.draftEnv.tool);
      console.log("这个分析剧本包含的词汇", this.draftEnv.word);
      console.log("这个分析剧本包含的句型", this.draftEnv.sentenceTemplate);
    },
    handleOk(e) {
      console.log(e);
      this.DraftEnvVisible = false;
    },
    //点击cancel
    handleCancel(e) {
      console.log("点击了取消按钮");
      this.DraftEnvVisible = false;
    },
    styleChange(recordId, item) {
      //当鼠标进入某一草稿剧本语句时，颜色改变
      console.log("进入p标签了");
      //this.styleActive=true;
      console.log(recordId + "#" + item.id);
      this.currentIndex = recordId + "#" + item.id;
    },
    styleLeave() {
      console.log("退出p标签了");
      // this.styleActive=false;
      this.currentIndex = "";
    },
    //根据分析剧本或者草稿剧本的每句话的serializationt提取拼接成content
    getContentFromSerialzation(serialization) {
      if (serialization.content && serialization.content.length > 0) {
        let ItemArr = serialization.content;
        let result = "";
        ItemArr.forEach((item) => {
          if (item.type == "stem") {
            result += item.name;
          } else if (item.type == "field") {
            result += item.content;
          }
        });
        return result;
      }
    },
    //数组去重去空
    uniqueArr(arr) {
      let newArr = [];
      let obj = {};

      arr.forEach((item) => {
        if (!obj[item]) {
          newArr.push(item);
          obj[item] = true;
        }
      });

      return newArr.filter(
        (item) => item != null && item != undefined && item != ""
      );
    },
    // 查看固件代码
    getFirmwareCode(event, record) {
      console.log(111);
      let analyseCodeDetail = this.$router.resolve({
        path: "/analyseCodeDetail/:content",
        query: {
          content: JSON.stringify(record),
        },
      });
      window.open(analyseCodeDetail.href, "_blank");
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
  min-width: 1185px;

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
    .header-right {
      // border:2px solid blue;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .nav {
        display: flex;
        //border:2px solid red;
        align-items: center;
      }
      span {
        margin-left: 60px;
        font-weight: bold;
        cursor: pointer;
      }
      span:hover {
        margin-left: 60px;
        font-weight: bold;
        color: purple;
      }
    }
  }

  .middleBusBck {
    width: 100%;
    height: 340px;
    background: url("../assets/business.png") no-repeat;
    background-position: center;
    background-size: 100% 100%;
    opacity: 0.8;
    padding-top: 120px;
    padding-left: 20px;
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

    .left {
      // background: #e8e9ea;
      background: white;

      border-radius: 5px;
    }
    .right {
      width: 100%;
      background: #fff;
      border-radius: 5px;
      .rightBread {
        color: rgb(237, 170, 170);
        height: 30px;
        background: #eff3f6;
      }
      .rightContent {
        padding: 0 20px;

        border-radius: 5px;
        .businessItem {
          //border: 2px solid rgb(14, 14, 14);
          padding-top: 50px;
          padding-bottom: 50px;
        }
        .businessLeft {
          width: 20%;
          //border: 2px solid rgb(237, 170, 170);
          .businessLeftTitle {
            color: black;
            font-size: 30px;
            font-style: oblique;
          }
          /* .businessLeftDesc{

          } */
        }
        .businessRight {
          width: 80%;
          // border: 2px solid rgb(171, 112, 112);
          margin-left: 40px;
          .draftItem {
            // border: 2px solid rgb(242, 13, 13);
            padding-top: 5px;
            padding-bottom: 5px;
            text-align: center;
            .draftItemImg img {
              width: 100px;
              height: 100px;
            }
          }
          .draftItem:hover {
            background: rgb(245, 244, 244);
          }
        }
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

  .xxx {
    display: block;
    width: 200px;
    height: 200px;
    background-color: aqua;
  }
}
</style>