<template>
  <a-layout id="components-layout-demo-custom-trigger">
    <a-layout-sider v-model="collapsed" :trigger="null" collapsible>
      <div class="logo" />
      <a-menu theme="dark" mode="inline" :default-selected-keys="['2']">
        <a-menu-item key="1" @click="changeMenu('checkWord')" >
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
          <a-breadcrumb-item>审核句型</a-breadcrumb-item>
          <!-- <a-breadcrumb-item>App</a-breadcrumb-item> -->
        </a-breadcrumb>
      <a-layout-content
        :style="{margin: '24px 16px', padding: '1px 24px',  background: '#fff', minHeight: '700px' }"
      >
      <router-view/>
      <a-table
        :columns="columns"
        :row-key="(record) => record.id"
        :pagination="pagination"
        :loading="tableLoading"
        :data-source="sentenceData"
        bordered
        :scroll="{ y: 600 }"
        @change="handleTableChange"
      >
        <a slot="name" slot-scope="text">{{ text }} </a>
        <span slot="types" slot-scope="types">
          <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
          <a-tag color="green" v-else-if="types == 2"> 自定义句型库 </a-tag>
          <a-tag color="red" v-else> 未知类型 </a-tag>
        </span>
<span slot="customUpdateTimeTitle">
          
          <a-dropdown>
              <a-menu slot="overlay" @click="onDatePickClick" >
                
                <a-menu-item key="1"> 一个月内 </a-menu-item>
                <a-menu-item key="2"> 一周内</a-menu-item>
                <a-menu-item key="3"> 今天</a-menu-item>
              </a-menu>
              <a> 修改时间<a-icon type="down" /> </a>
            </a-dropdown>
            </span>
        <span slot="statu" slot-scope="statu">
          <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
          <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
          <a-tag color="orange" v-else-if="statu == 2">
            审核通过开发中
          </a-tag>
          <a-tag color="green" v-else-if="statu == 3"> 开发完成启用中 </a-tag>
          <a-tag color="red" v-else-if="statu == 4"> 已禁用 </a-tag>
          <a-tag color="red" v-else> 未知类型 </a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a-popconfirm
            v-if="record.status == 0"
            type="primary"
            cancelText="取消"
            okText="确定"
            title="确认审核并启用?"
            @confirm="handleSentenceCkeck(record, 2)"
          >
            <a-button type="primary" size="small" ghost> 审核通过请开发 </a-button>
            <a-divider type="vertical" />
          </a-popconfirm>

          <a-popconfirm
            v-if="record.status == 0"
            type="primary"
            cancelText="取消"
            okText="确定"
            title="确认审核不通过?"
            @confirm="handleSentenceCkeck(record, 1)"
          >
            <a-button type="danger" size="small" ghost> 审核不通过 </a-button>
          </a-popconfirm>

          <a-popconfirm
            v-else-if="record.status == 2"
            type="primary"
            cancelText="取消"
            okText="确定"
            title="确认重新启动使用?"
            @confirm="handleSentenceCkeck(record, 3)"
          >
            <a-button type="primary" size="small" ghost>
              开发完成启用句型
            </a-button>
          </a-popconfirm>

          <a-popconfirm
            v-else-if="record.status == 3"
            type="primary"
            cancelText="取消"
            okText="确定"
            title="确认禁止使用?"
            @confirm="handleSentenceCkeck(record, 4)"
          >
            <a-button type="danger" size="small" ghost> 禁用 </a-button>
          </a-popconfirm>

          <a-popconfirm
            v-else-if="record.status == 4"
            type="primary"
            cancelText="取消"
            okText="确定"
            title="确认启用使用?"
            @confirm="handleSentenceCkeck(record, 3)"
          >
            <a-button type="primary" size="small" ghost> 启用 </a-button>
          </a-popconfirm>

          <a-button v-else size="small"> 无操作 </a-button>
        </span>
      </a-table>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
import { getUserName } from "../common/env";
import SentenceService from "@/service/sentence_service";
const sentenceService = new SentenceService();
const columns = [
  {
    title: "句型名字",
    dataIndex: "name",
    key: "name",
    width: 100,
    scopedSlots: { customRender: "name" },
    algin:'center',
  },
  { title: "描述", dataIndex: "description", key: "description" },
  { title: "例子", dataIndex: "example", key: "example" },
  { title: "序列化表示", dataIndex: "serialization", key: "serialization" },
  { title: "编辑者", dataIndex: "creatorName", key: "creatorName", width: 100,algin:'center', },
  { //title: "创建时间",
   dataIndex: "updateTime", 
   key: "updateTime" ,
   slots:{title:"customUpdateTimeTitle"},
   algin:'center',},
  {
    title: "类型",
    dataIndex: "type",
    key: "type",
    scopedSlots: { customRender: "types" },
    algin:'center',
  },
  {
    title: "状态",
    dataIndex: "status",
    key: "status",
    scopedSlots: { customRender: "statu" },
    algin:'center',
  },
  {
    title: "操作",
    dataIndex: "",
    key: "x",
    scopedSlots: { customRender: "action" },
    width: 120,
    algin:'center',
  },
];

const sentenceTamplate = JSON.stringify({
  id: 0,
  type: "",
  status: "",
  name: "",
  serialization: "",
  example: 0,
  creatorId: 0,
  creatorName: "",
  createTime: 0,
  updateTime: 0,
  description: "",
});
export default {
  data() {
    return {
      userName: getUserName(),
      collapsed: false,
      sentenceData: [],
      columns,
      pagination: {
        page: 1,
        count: 10,
        total: 0,
      },
      tableLoading: false,
      confirmLoading: false,
      labelCol: { span: 6 },
      wrapperCol: { span: 12 },
      title: "",
   
      form: JSON.parse(sentenceTamplate),
      rules: {},
    };
  },
   created() {
    sentenceService.getAllSentenceInChecking({
        onSuccess: (model, fModel, json) => {
          console.log(json);
          console.log("这是帮语网站中使用的句型");
          this.sentenceData = json.data.rows.sentence .sort((a, b) => {
                //根据句型创建的先后顺序进行展示
                const date1 = new Date(a.updateTime).getTime();
                const date2 = new Date(b.updateTime).getTime();
                return date2 - date1;
              });;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
      });
  },
  methods:{
    refresh() {
      sentenceService.getAllSentence({
        onSuccess: (model, fModel, json) => {
          console.log(json);
          this.sentenceData = json.data.rows.sentence
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
      });
    }, 
    toHome() {
      this.$router.push({ name: "Home" });
    },
    //路由内容切换
    changeMenu(route){
      console.log(route)
      //获取路由对象并切换
      this.$router.push(route);
      this.refresh();
            },
    
       async handleSentenceCkeck(t, status) {
      this.confirmLoading = true;

      this.form = t;
      this.form.status = status;

      try {
        let result;
        result = await sentenceService.updateSentence(this.form);
        this.confirmLoading = false;
        this.refresh();
      } catch (msg) {
        this.confirmLoading = false;
        console.log(msg);
      }
    },
    handleTableChange(pagination, filters, sorter) {
      //<a-table>里面有一个 @change="handleTableChange"
      console.log(pagination);
      const pager = { ...this.pagination };
      pager.page = pagination.current;
      this.pagination = pager;
      this.fetch({
        count: pagination.pageSize,
        page: pagination.current,
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...filters,
      });
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
    },
    async fetch(params = {}) {
      this.tableLoading = true;
      const result = await sentenceService.getAllSentence();
      this.data = result.rows;

      this.pagination.total = result.total;
      this.tableLoading = false;
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
