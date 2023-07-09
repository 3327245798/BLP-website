<template>
  <a-layout id="components-layout-demo-custom-trigger">
    <a-layout-sider v-model="collapsed" :trigger="null" collapsible>
      <div class="logo" />
      <a-menu theme="dark" mode="inline" :default-selected-keys="['1']">
        <a-menu-item key="1" @click="changeMenu('workerWordPage')">
          <a-icon type="user" />
          <span>创建词汇</span>
        </a-menu-item>
        <a-menu-item key="2" @click="changeMenu('workerSentencePage')">
          <a-icon type="video-camera" />
          <span>创建句型</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding: 0">
        <a-icon
          class="trigger"
          :type="collapsed ? 'menu-unfold' : 'menu-fold'"
          @click="() => (collapsed = !collapsed)"
        />
        <span>帮语工者{{ userName }}，你好</span>
      </a-layout-header>
      <a-breadcrumb style="margin: 16px">
        <a-breadcrumb-item @click.native="toHome">首页</a-breadcrumb-item>
        <a-breadcrumb-item>创建词汇</a-breadcrumb-item>
        <!-- <a-breadcrumb-item>App</a-breadcrumb-item> -->
      </a-breadcrumb>
      <a-layout-content
        :style="{
          margin: '24px 16px',
          padding: '1px 24px',
          background: '#fff',
          minHeight: '700px',
        }"
      >
        <router-view />
        <a-tabs default-active-key="1" @change="callback">
          <a-tab-pane key="1">
            <span slot="tab">
              <a-icon type="apple" />
              创建词汇
            </span>
            <a-button class="a" type="primary" @click="openAddWord()">
              新增词汇
            </a-button>
            <a-modal
              :title="wordFormTitle"
              :visible="addWordVisible"
              :confirm-loading="confirmLoading"
              okText="提交"
              cancelText="取消"
              @ok="handleWordOk('wordForm')"
              @cancel="handleWordCancel"
              :mask-closable="false"
            >
              <a-form-model
                ref="wordForm"
                :model="wordForm"
                :label-col="labelCol"
                :wrapper-col="wrapperCol"
                :rules="rules"
              >
                <a-form-model-item
                  label="词汇"
                  prop="name"
                  :rules="[
                    {
                      required: true,
                      message: '请输入词汇名字',
                      trigger: 'blur',
                    },
                  ]"
                >
                  <a-input v-model="wordForm.name" />
                </a-form-model-item>
                <a-form-model-item
                  label="描述"
                  prop="description"
                  :rules="[
                    {
                      required: true,
                      message: '请输入词汇描述',
                      trigger: 'blur',
                    },
                  ]"
                >
                  <a-textarea v-model="wordForm.description" />
                </a-form-model-item>
              </a-form-model>
            </a-modal>
            <a-table
              :columns="wordColumns"
              :data-source="wordData"
              bordered
              :rowKey="
                (record, index) => {
                  return index;
                }
              "
              :loading="tableLoading"
              @change="handleTableChange"
              @expand="handleWordBindUnit"
              class="components-table-demo-nested"
            >
            <span slot="statu" slot-scope="text, record">
              <a-tag v-if="text='1'" color="purple" >审核中</a-tag>
              <a-tag v-else-if="text='2'" color="green">审核通过</a-tag>
              <a-tag v-else-if="text='3'" color="orange">审核未通过</a-tag>
              <a-tag v-else color="#f50">禁用</a-tag>

            </span>
              <span slot="action" slot-scope="text, record">
                <a-button
                  @click="openModifyWord(record)"
                  type="primary"
                  size="small"
                  ghost
                >
                  编辑
                </a-button>
                <a-divider type="vertical" />
                <a-popconfirm
                  cancelText="取消"
                  okText="确定"
                  title="确认删除?"
                  @confirm="deleteWord(record)"
                >
                  <a-button type="danger" size="small" ghost> 删除 </a-button>
                </a-popconfirm>
                <a-divider type="vertical" />
                 <a-button
                  type="primary"
                  size="small"
                  ghost
                >
                  固件
                </a-button>
              </span>
              <div
                slot="expandedRowRender"
                slot-scope="record, index"
                style="margin: 0"
              >
                <a-button
                  class="a"
                  type="primary"
                  @click="openAddUnit(record.id, record, index)"
                >
                  绑定固件
                </a-button>
                <a-table
                  :columns="innerUnitColumns"
                  :data-source="record.unitData"
                  :pagination="false"
                  bordered
                  :rowKey="
                    (record, index) => {
                      return index;
                    }
                  "
                >
                 
                  <span
                    slot="operation"
                    slot-scope="text"
                    class="table-operation"
                  >
                    <a>取消绑定</a>
                   <!--  <a>Stop</a> -->
                   <!--  <a-dropdown>
                      <a-menu slot="overlay">
                        <a-menu-item> Action 1 </a-menu-item>
                        <a-menu-item> Action 2 </a-menu-item>
                      </a-menu>
                      <a> More <a-icon type="down" /> </a>
                    </a-dropdown> -->
                  </span>
                </a-table>
                <a-modal
                  :title="unitFormTitle"
                  :visible="addUnitVisible"
                  :confirm-loading="confirmLoading"
                  okText="提交"
                  cancelText="取消"
                  @ok="handleUnitOk('unitForm')"
                  @cancel="handleUnitCancel"
                  :mask-closable="false"
                >
                  <a-form-model
                    ref="unitForm"
                    :model="unitForm"
                    :label-col="labelCol"
                    :wrapper-col="wrapperCol"
                    :rules="rules"
                  >
                    <a-form-model-item label="工具" prop="toolId">
                      <!-- :v-model="tools" -->
                      <a-select
                        v-model="unitForm.toolId"
                        show-search
                        @change="handleSelectToolChange"
                        :default-active-first-option="false"
                        :show-arrow="true"
                        :not-found-content="null"
                        placeholder="选择或搜索工具"
                      >
                        <a-select-option
                          :key="t.toolId"
                          :value="t.toolId"
                          v-for="t in tools"
                        >
                          {{ t.toolName }}
                        </a-select-option>
                      </a-select>
                    </a-form-model-item>

                    <a-form-model-item
                      label="部件中文名"
                      prop="name"
                      :rules="[
                        {
                          required: true,
                          message: '请输入内容',
                          trigger: 'blur',
                        },
                      ]"
                    >
                      <a-input v-model="unitForm.name" />
                    </a-form-model-item>
                    <a-form-model-item
                      label="部件名"
                      prop="action"
                      :rules="[
                        {
                          required: true,
                          message: '请输入内容',
                          trigger: 'blur',
                        },
                      ]"
                    >
                      <a-input v-model="unitForm.action" />
                    </a-form-model-item>

                    <a-form-model-item label="参数" prop="params">
                      <a-textarea v-model="unitForm.params" />
                    </a-form-model-item>
                    <a-form-model-item label="返回值" prop="result">
                      <a-textarea v-model="unitForm.result" />
                    </a-form-model-item>
                    <a-form-model-item label="参数实体类型" prop="clazz">
                      <a-input v-model="unitForm.clazz" />
                    </a-form-model-item>
                  </a-form-model>
                </a-modal>
              </div>
            </a-table>
          </a-tab-pane>
          <a-tab-pane key="2">
            <span slot="tab">
              <a-icon type="android" />
              我的词汇
            </span>
            这里是我创建的词汇
          </a-tab-pane>
        </a-tabs>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
import { getUserName } from "../common/env";
import WordService from "@/service/word_service";
import BandService from "@/service/band_service";
import UnitService from "@/service/unit_service";

const wordService = new WordService();
const bandService = new BandService();
const unitService = new UnitService();

const wordColumns = [
  { title: "词汇", dataIndex: "name", key: "name" ,width:120},
  { title: "描述", dataIndex: "description", key: "description" ,width:160,},
  { title: "编辑者", dataIndex: "creatorName", key: "creatorName" ,width:80},
  {
    title: "状态",
    dataIndex: "status",
    key: "status",
    scopedSlots: { customRender: "statu" },
    width:60
  },
    {
    title: "词汇语义",
    dataIndex: "meaning",
    key: "meaning",
    width:120
  },
  { title: "更新时间", dataIndex: "updateTime", key: "updateTime" ,width:130,},
  {
    title: "操作",
    dataIndex: "action",
    key: "x",
    scopedSlots: { customRender: "action" },
    width:180
  },
];
const wordData = [
  {
    name: "今天天气",
    description: "对天气工具的当天天气部件的调用",
    creatorName: "王哲",
    status: "审核中",
    updateTime: "2022:8:25 12:13:14",
  },
];
const wordTamplate = JSON.stringify({
  id: 0,
  name: "",
  creator_id: "",
  meaning:"",
  update_time: "",
  status: 0,
  description: "",
  unitid: 0,
});
const innerUnitColumns = [
  { title: "工具", dataIndex: "toolName", key: "toolName" },
  { title: "工具id", dataIndex: "toolId", key: "toolId" },

  //{ title: "部件别名", dataIndex: "name", key: "name" },
  { title: "部件名", dataIndex: "action", key: "action" },
  { title: "参数", dataIndex: "params", key: "params" },
  { title: "返回值", dataIndex: "result", key: "result" },
  { title: "参数实体", dataIndex: "clazz", key: "clazz" },
  {
    title: "操作",
    dataIndex: "",
    key: "x",
    scopedSlots: { customRender: "operation" },
  },
];
const innerUnitData = [];
const unitTamplate = JSON.stringify({
  name: "",
  action: "",
  params: "",
  result: "",
  clazz: "",

  toolId: "",
  toolName: "",
  bandId: 0,
  bandName: "",

  wordId: 0,
});

export default {
  name: "WordPage",
  components: {},
  data() {
    return {
      userName: getUserName(),
      wordData,
      wordColumns,
      innerUnitColumns,
      innerUnitData,
      unitForm: JSON.parse(unitTamplate),
      curentEditRecordIndex:null,
      wordForm: JSON.parse(wordTamplate),
      rules: {
        name: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },
      actionType: "ADD",
      wordFormTitle: "",
      addWordVisible: false,
      unitFormTitle: "",
      addUnitVisible: false,
      confirmLoading: false,
      tableLoading: false,
      tools: [],
    };
  },
  computed: {},
  beforeCreate() {},
  created() {
    this.refresh();
  },
  watch: {},
  methods: {
    onChange(value) {
      console.log(value);
    },
    refresh() {
      wordService.getAllPublicWordNoUnit({
        onSuccess: (model, fModel, json) => {
          console.log(json);
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
      });
    },
    //获取所有绑定词汇的固件
    getAllUnitsByWordID(recordId, record) {
      console.log("获取已绑定固件的词汇");
      console.log(this.wordData);
      unitService.getAllUnitsByWordID(
        {
          wordId: recordId,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.innerUnitData = json.data.rows.unit;

            this.$set(record,'unitData', json.data.rows.unit)
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },

    //路由内容切换
    changeMenu(route) {
      console.log(route);
      //获取路由对象并切换
      this.$router.push(route);
    },
    toHome() {
      this.$router.push({ name: "Home" });
    },
    //处理词汇
    async handleWordOk(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        }
        this.submitWord();
      });
    },
    handleWordCancel() {
      this.addWordVisible = false;
    },
    async submitWord() {
      this.confirmLoading = true;

      try {
        let result;
        if (this.actionType == "ADD") {
          // this.wordForm.creator_id = getUserId();
          //this.wordForm.status = 0;
          //this.wordForm.band_obj_id = getBandId();
          result = await wordService.createWord(this.wordForm);
          this.$message.success("添加词汇成功");
        } else {
          result = await wordService.updateWord(this.wordForm);
          this.$message.success("修改词汇成功");
        }

        this.confirmLoading = false;
        this.addWordVisible = false;
        this.refresh();
      } catch (msg) {
        this.confirmLoading = false;
      }
    },
    openAddWord() {
      this.actionType = "ADD";
      this.addWordVisible = true;
      this.wordFormTitle = "新增词汇";
      this.wordForm = JSON.parse(wordTamplate);
    },
    openModifyWord(word) {
      this.actionType = "Modify";
      this.addWordVisible = true;
      this.wordFormTitle = "修改词汇";
      this.wordForm = word;

      let has = this.wordData.findIndex((t) => t.name == word.name) > -1;
      if (!has) {
        this.wordData.push({
          name: word.name,
          formName,
        });
      }
    },
    async deleteWord(word) {
      await wordService.deleteWord(word);
      //this.fetch(this.pagination);
      this.refresh();
    },
    //处理固件
    async handleUnitOk(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        }
        this.submitUnit();
      });
    },
    handleUnitCancel() {
      this.addUnitVisible = false;
    },
    async submitUnit() {
      this.confirmLoading = true;

      let index = this.tools.findIndex(t => t.toolId == this.unitForm.toolId)
      this.unitForm.toolName = this.tools[index].toolName

      try {
        let result;
        if (this.actionType == "ADD") {
          result = await unitService.bindUnit(this.unitForm);
          this.$message.success("绑定固件成功");
        } else {
          result = await unitService.updateBindUnit(this.unitForm);
          this.$message.success("修改固件成功");
        }

        this.confirmLoading = false;
        this.addUnitVisible = false;
        this.getAllUnitsByWordID(this.wordData[this.curentEditRecordIndex].id, this.wordData[this.curentEditRecordIndex]);
        
      } catch (msg) {
        this.confirmLoading = false;
      }
    },
    openAddUnit(id, record,index) {
      this.actionType = "ADD";
      this.addUnitVisible = true;
      this.unitFormTitle = "绑定固件";
      this.unitForm = JSON.parse(unitTamplate);
      this.unitForm.wordId = id;
      this.curentEditRecordIndex = index
      console.log(index)
    },
    openModifyUnit(unit) {
      this.actionType = "Modify";
      this.addUnitVisible = true;
      this.unitFormTitle = "修改固件";
      this.unitForm = unit;

      let has =
        this.innerUnitData.findIndex((t) => t.toolName == unit.toolName) > -1;
      if (!has) {
        this.innerUnitData.push({
          toolName: unit.toolName,
        });
      }
    },
    handleSelectToolChange(value) {
      console.log(value);
    },
    //处理表格分页
    handleTableChange(pagination, filters, sorter) {
      //<a-table>里面有一个 @change="handleTableChange"
      console.log("这是pagination的内容");
      console.log(pagination);
      console.log("这是filters的内容");
      console.log(filters);
      console.log("这是sorter的内容");
      console.log(sorter);

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
    async fetch(params = {}) {
      this.tableLoading = true;

      const result = await wordService.getAllPublicWordNoUnit(); //返回结果是一个Model
      console.log("帮语词汇工者执行了fetch方法");
      console.log(result);
      this.data = result.rows;
      this.pagination.total = result.total;
      this.tableLoading = false;
    },
    //展开词汇时绑定固件，这时从后台获取帮区的工具
    handleWordBindUnit(expanded, record) {
      bandService.getToolsInBand({
        onSuccess: (model, fModel, json) => {
          // console.log(json.data.tools.rows);
          console.log(record.id);

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
      this.getAllUnitsByWordID(record.id, record);
    },

    getCheckedWord() {
      wordService.getCheckedWord({
        onSuccess: (model, fModel, json) => {
          console.log(json); //审核通过的公共词汇
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
      });
    },
    getAllStatusWord() {
      wordService.getAllPublicWord({
        //查询所有词汇
        onSuccess: (model, fModel, json) => {
          console.log(json);
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
      });
    },

    onSearch() {
      if (this.searchType == "编辑者") {
        this.onSearchByCreatorName(this.searchName);
      } else {
        this.onSearchByStatus(this.searchName);
      }
    },
    onSearchByCreatorName(value) {
      console.log(value);
      if (value == "") {
        this.refresh();
      } else {
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
              console.log(msg, json);
            },
          }
        );
      }
    },
    onSearchByStatus(value) {
      console.log(value);
      if (value == "") {
        this.refresh();
      } else {
        let status = 0;
        if (value == "禁用") {
          status = 0;
        } else if (value == "审核中") {
          status = 1;
        } else if (value == "审核未通过") {
          status = 2;
        } else if (value == "审核通过并启用") {
          status = 3;
        }
        wordService.getPublicWordByStatus(
          {
            status: status,
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
  },
};
</script>
<style lang="scss">
.wordpage-container {
  height: 100%;
  display: flex;
  margin-left: 80px;
  margin-right: 120px;
  .wordku-container {
    flex: 1;
    position: relative;
    background: #fff;
    height: 100%;
    min-width: 1165px;
    padding-left: 30px;
    padding-top: 10px;
  }
  .topbutton {
    display: flex;
    //justify-content: space-between;
    .a {
      margin-right: 18px;
    }
  }
}
</style>