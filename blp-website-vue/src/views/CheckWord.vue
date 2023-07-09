<template>
  <a-layout id="components-layout-demo-custom-trigger">
    <a-layout-sider v-model="collapsed" :trigger="null" collapsible>
      <div class="logo" />
      <a-menu theme="dark" mode="inline" :default-selected-keys="['1']">
        <a-menu-item key="1"  @click="changeMenu('checkWord')">
          <a-icon type="user" />
          <span>审核词汇</span>
        </a-menu-item>
        <a-menu-item key="2" @click="changeMenu('checkSentence')">
          <a-icon type="video-camera" />
          <span>审核句型</span>
        </a-menu-item>
       <!--  <a-menu-item key="3">
          <a-icon type="upload" />
          <span>nav 3</span>
        </a-menu-item> -->
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding: 0">
        <a-icon
          class="trigger"
          :type="collapsed ? 'menu-unfold' : 'menu-fold'"
          @click="() => (collapsed = !collapsed)"
        />
        <span>帮语审核员{{userName}}，你好</span>
      </a-layout-header>
      <a-breadcrumb style="margin:16px">
          <a-breadcrumb-item  @click.native="toHome">首页</a-breadcrumb-item>
          <a-breadcrumb-item>审核词汇</a-breadcrumb-item>
          <!-- <a-breadcrumb-item>App</a-breadcrumb-item> -->
        </a-breadcrumb>
      <a-layout-content
        :style="{margin: '24px 16px', padding: '1px 24px',  background: '#fff', minHeight: '700px' }"
      >
      <a-tabs default-active-key="1" @change="callback">
    <a-tab-pane key="1">
      <span slot="tab">
        <a-icon type="apple" />
        全部词汇
      </span>
     <a-table
      :columns="columns"
      :row-key="(record) => record.id"
      :pagination="pagination"
      :loading="tableLoading"
      :data-source="wordData"
      bordered
    >
      <!-- :defaultExpandedRowKeys="expandline" -->
      <span slot="types" slot-scope="types">
        <a-tag color="red" v-if="types == 0"> 禁用 </a-tag>
        <a-tag color="cyan" v-else-if="types == 1"> 名词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 2"> 动词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 3"> 形容词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 4"> 副词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 5"> 数词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 6"> 量词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 7"> 代词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 8"> 叹词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 9"> 拟声词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 10"> 介词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 11"> 连词 </a-tag>
        <a-tag color="green" v-else> 未知类型 </a-tag>
      </span>
      <span slot="isRanges" slot-scope="isRanges">
        <a-tag color="green" v-if="isRanges == 0"> 原生公共词汇 </a-tag>
        <a-tag color="green" v-else-if="isRanges == 1">
          其他帮区私有词汇
        </a-tag>
        <a-tag color="green" v-else-if="isRanges == 2">
          其他帮区拉取的公共词汇
        </a-tag>
        <a-tag color="green" v-else> 未知类型 </a-tag>
      </span>

      <span slot="statu" slot-scope="statu">
        <a-tag color="red" v-if="statu == 0"> 未审查 </a-tag>
        <a-tag color="orange" v-else-if="statu == 1"> 审查中 </a-tag>
        <a-tag color="blue" v-else-if="statu == 2"> 启用中 </a-tag>
        <a-tag color="orange" v-else-if="statu == 3"> 已停用 </a-tag>
        <a-tag color="red" v-else> 未知类型 </a-tag>
      </span>

      <div slot="expandedRowRender" slot-scope="record">
        <!-- <manageVerfyUnit
          :id="record.id"
          :bandId="record.bandObjId"
        ></manageVerfyUnit> -->
      </div>

      <span slot="action" slot-scope="text, record">
        <a-popconfirm
          v-if="record.status == 1"
          type="primary"
          cancelText="取消"
          okText="确定"
          title="确认启用?"
          @confirm="handlePublicWord(record,2)"
        >
          <a-button type="primary" size="small" ghost> 启用 </a-button>
        </a-popconfirm>

        <a-popconfirm
          v-else-if="record.status == 3"
          type="primary"
          cancelText="取消"
          okText="确定"
          title="确认重新启动使用?"
          @confirm="handlePublicWord(record,2)"
        >
          <a-button type="danger" size="small" ghost> 重新启用 </a-button>
        </a-popconfirm>

        <a-popconfirm
          v-else-if="record.status == 2"
          type="primary"
          cancelText="取消"
          okText="确定"
          title="禁用操作不可逆,请确认禁止使用?"
          @confirm="handlePublicWord(record,3)"
        >
          <a-button type="primary" size="small" ghost> 禁用 </a-button>
        </a-popconfirm>

        <a-button
          v-else
          size="small"
        >
          无操作
        </a-button>
      </span>
    </a-table>
    </a-tab-pane>
    <a-tab-pane key="2">
      <span slot="tab">
        <a-icon type="android" />
        待审核词汇
      </span>
      <a-table
      :columns="columns"
      :row-key="(record) => record.id"
      :pagination="pagination"
      :loading="tableLoading"
      :data-source="wordData"
      bordered
    >
      <!-- :defaultExpandedRowKeys="expandline" -->
      <span slot="types" slot-scope="types">
        <a-tag color="red" v-if="types == 0"> 禁用 </a-tag>
        <a-tag color="cyan" v-else-if="types == 1"> 名词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 2"> 动词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 3"> 形容词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 4"> 副词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 5"> 数词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 6"> 量词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 7"> 代词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 8"> 叹词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 9"> 拟声词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 10"> 介词 </a-tag>
        <a-tag color="cyan" v-else-if="types == 11"> 连词 </a-tag>
        <a-tag color="green" v-else> 未知类型 </a-tag>
      </span>
      <span slot="isRanges" slot-scope="isRanges">
        <a-tag color="green" v-if="isRanges == 0"> 原生公共词汇 </a-tag>
        <a-tag color="green" v-else-if="isRanges == 1">
          其他帮区私有词汇
        </a-tag>
        <a-tag color="green" v-else-if="isRanges == 2">
          其他帮区拉取的公共词汇
        </a-tag>
        <a-tag color="green" v-else> 未知类型 </a-tag>
      </span>

      <span slot="statu" slot-scope="statu">
        <a-tag color="red" v-if="statu == 0"> 未审查 </a-tag>
        <a-tag color="orange" v-else-if="statu == 1"> 审查中 </a-tag>
        <a-tag color="blue" v-else-if="statu == 2"> 启用中 </a-tag>
        <a-tag color="orange" v-else-if="statu == 3"> 已停用 </a-tag>
        <a-tag color="red" v-else> 未知类型 </a-tag>
      </span>

      <div slot="expandedRowRender" slot-scope="record">
        <!-- <manageVerfyUnit
          :id="record.id"
          :bandId="record.bandObjId"
        ></manageVerfyUnit> -->
      </div>

      <span slot="action" slot-scope="text, record">
        <a-popconfirm
          v-if="record.status == 1"
          type="primary"
          cancelText="取消"
          okText="确定"
          title="确认启用?"
          @confirm="handlePublicWord(record,2)"
        >
          <a-button type="primary" size="small" ghost> 启用 </a-button>
        </a-popconfirm>

        <a-popconfirm
          v-else-if="record.status == 3"
          type="primary"
          cancelText="取消"
          okText="确定"
          title="确认重新启动使用?"
          @confirm="handlePublicWord(record,2)"
        >
          <a-button type="danger" size="small" ghost> 重新启用 </a-button>
        </a-popconfirm>

        <a-popconfirm
          v-else-if="record.status == 2"
          type="primary"
          cancelText="取消"
          okText="确定"
          title="确认禁止使用?"
          @confirm="handlePublicWord(record,3)"
        >
          <a-button type="primary" size="small" ghost> 禁用 </a-button>
        </a-popconfirm>

        <a-button
          v-else
          size="small"
        >
          无操作
        </a-button>
      </span>
    </a-table>
    </a-tab-pane>
  </a-tabs>
       
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
import { getUserName } from "../common/env";
import WordService from"@/service/word_service";
//import manageVerfyUnit from "./manage_verfy_unit.vue";
const wordService=new WordService();
const columns = [
  { title: "词汇", dataIndex: "name", key: "name",width:80 },
  { title: "描述", dataIndex: "description", key: "description",width:160,ellipsis:true },
  { title: "编辑者", dataIndex: "creatorName", key: "creatorName" ,width:80},
  { title: "工具", dataIndex: "toolName", key: "toolName",width:80 },
  { title: "部件", dataIndex: "unitName", key: "unitName",width:120 },

  {
    title: "状态",
    dataIndex: "status",
    key: "status",
    scopedSlots: { customRender: "statu" },
    width:160
  },
  { title: "更新时间", dataIndex: "updateTime", key: "updateTime" ,width:160},
  {
    title: "操作",
    dataIndex: "",
    key: "x",
    scopedSlots: { customRender: "action" },
    width:160
  },
];

const wordTamplate = JSON.stringify({
  id: 0,
  name: "",
  creatorName: "",
  update_time: "",
  //isTypeIn: 1,
  //isRange: 0,
  status: 0,
  description: "",
  toolName:"",
  unitName:"",
    //unitid: 0,
  //band_obj_id: 0,
});
export default {
  data() {
    return {
      userName: getUserName(),
      collapsed: false,
     columns,
     wordData:[],
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
      title: "",
      actionType: "ADD",
      form: JSON.parse(wordTamplate),
      rules: {}
    };
  },
   components: {
   /*  manageVerfyUnit,
    props: ["id"], */
  },
  created(){
    this.refreshAllWord();
    /* wordService.getInCheckingWord({
       onSuccess: (model, fModel, json) => {
          console.log(json);
          console.log("这是帮语网站中使用的词汇");
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
    }) */
  },
  methods:{
       refreshAllWord(){
      wordService.getAllPublicWord({
       onSuccess: (model, fModel, json) => {
          console.log(json);
          console.log("这是帮语网站中所有的词汇");
          this.wordData = json.data.rows.word
           .sort((a, b) => {
                //根据词汇创建的先后顺序进行展示
                const date1 = new Date(a.updateTime).getTime();
                const date2 = new Date(b.updateTime).getTime();
                return date2 - date1;
              });
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
    })
    },
    
    refreshChecking(){
        wordService.getInCheckingWord({
       onSuccess: (model, fModel, json) => {
          console.log(json);
          console.log("这是帮语网站中待审核的词汇");
          this.wordData = json.data.rows.word .sort((a, b) => {
                //根据词汇创建的先后顺序进行展示
                const date1 = new Date(a.updateTime).getTime();
                const date2 = new Date(b.updateTime).getTime();
                return date2 - date1;
              });;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
    })
    },
    callback(key){
       if (key == 1) {
          this.refreshAllWord();
        } else if (key == 2) {
          this.refreshChecking();
        }
    },
    toHome() {
      this.$router.push({ name: "Home" });
    },
    //路由内容切换
    changeMenu(route){
      console.log(route)
      //获取路由对象并切换
      this.$router.push(route);
      this.refreshAllWord();
            },
 
    async handlePublicWord(t,status){
      this.confirmLoading = true;
      this.form = t;
      this.form.status = status;
      try {
        let result;
        result = await wordService.updateWord(this.form);
        this.confirmLoading = false;
        this.refresh();
      } catch (msg) {
        this.confirmLoading = false;
        console.log(msg);
      }

    },
    
  }
};
</script>
<style>
#components-layout-demo-custom-trigger .trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

#components-layout-demo-custom-trigger .trigger:hover {
  color: #1890ff;
}

#components-layout-demo-custom-trigger .logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px;
}
</style>
