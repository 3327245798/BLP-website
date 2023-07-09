<template>
    <div>
          <a-button class="a" type="primary"  @click="openAddSentence">
        新增句型
      </a-button>
     <a-table
        bordered
        :rowKey="
          (record, index) => {
            return index;
          }
        "
        :loading="tableLoading"
        @change="handleTableChange"
        :columns="columns"
        :data-source="sentenceData"
      >
        <a slot="name" slot-scope="text" style="font-size:15px">{{ text }}</a>
        <span slot="customTitle" ><a-icon type="smile-o" /> 名称</span>

      
        <span slot="creatorName" slot-scope="text">
          <a-tag 
          style="font-size:15px"
          color="purple">
            {{ text }}
          </a-tag>
        </span>
        <span slot="type" slot-scope="type">
          <a-tag
          style="font-size:15px"
            v-for="tag in type"
            :key="tag"
            :color="tag === '1' ? 'green' : 'geekblue'"
          >
            {{ tag === "1" ? "标准型句型库" : "用户自定义句型库" }}
          </a-tag>
        </span>
        <span slot="sentenceStatus" slot-scope="text" >
          <a-tag
          style="font-size:15px"
          v-if="text=='0'"
           :key="tag"
          color='orange'>、
            {{"审核中"}}
          </a-tag>
          <a-tag
          style="font-size:15px"
          v-else-if="text=='1'"
           :key="tag"
          color='purple'>
            {{"审核未通过"}}
          </a-tag>
           <a-tag
           style="font-size:15px"
          v-else-if="text=='2'"
           :key="tag"
          color='cyan'>
            {{"审核通过开发中"}}
          </a-tag>
           <a-tag
           style="font-size:15px"
          v-else-if="text=='3'"
           :key="tag"
          color='green'>
            {{"开发完成启用中"}}
          </a-tag>
           <a-tag
           style="font-size:15px"
          v-else-if="text=='4'"
           :key="tag"
          color='red'>
            {{"禁用"}}
          </a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a-button
            @click="openModifySentence(record)"
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
            @confirm="deleteSentence(record)"
          >
            <a-button type="danger" size="small" ghost> 删除 </a-button>
          </a-popconfirm>
        </span>
      </a-table>
      <a-modal
        :title="title"
        :visible="addSentenceVisible"
        :confirm-loading="confirmLoading"
        okText="提交"
        cancelText="取消"
        @ok="handleOk('sentenceForm')"
        @cancel="handleCancel"
        :mask-closable="false"
      >
        <a-form-model
          ref="sentenceForm"
          :model="form"
          :label-col="labelCol"
          :wrapper-col="wrapperCol"
        >
          <a-form-model-item
            label="句型库类型"
            prop="type"
            :rules="[
              { required: true, message: '请输入内容', trigger: 'blur' },
            ]"
          >
            
            <a-radio-group
              name="sentenceLibType"
              v-model="form.type"
              :default-value="1"
            >
              <a-radio value="1"> 标准型句型库 </a-radio>
              <a-radio value="2"> 用户自定义型句型库 </a-radio>
            </a-radio-group>
          </a-form-model-item>

          <a-form-model-item
            label="句型名称"
            prop="name"
            
            :rules="[
              { required: true, message: '请选择句型名称', trigger: 'blur' },
            ]"
          >
            <a-select
              v-model="form.name"
              placeholder="请选择句型名称"
            >
              <!-- 
              :filter-option="filterOption"
                :show-arrow="false"
                show-search
                 :default-active-first-option="true"
                :not-found-content="null" -->

              <a-select-option
                :key="st.sentenceNameId"
                v-for="st in sentenceNames"
                :value="st.name"
              >
                {{ st.name }}
              </a-select-option>
            </a-select>
          </a-form-model-item>

          <a-form-model-item
            label="句型描述"
            prop="description"
             placeholder="请输入描述"
            :rules="[
              { required: true, message: '请输入描述', trigger: 'blur' },
            ]"
          >
            <a-input v-model="form.description" />
          </a-form-model-item>
          <a-form-model-item
            label="句型序列化表示"
            prop="serialization"
            placeholder="请输入序列化表示"
            :rules="[
              { required: true, message: '序列化表示', trigger: 'blur' },
            ]"
          >
            <a-input v-model="form.serialization" />
          </a-form-model-item>

          <a-form-model-item label="句子" prop="example">
            <a-textarea
              v-model="form.example"
              placeholder="Autosize height with minimum and maximum number of lines"
              :auto-size="{ minRows: 2, maxRows: 6 }"
            />
          </a-form-model-item>
        
        </a-form-model>
        <template slot="footer">
          <a-button @click="handleCancel">取消</a-button>
          <a-button type="primary" @click="handleSaveToLocale('sentenceForm')">保存到本地</a-button>
          <a-button type="primary" @click="handleOk('sentenceForm')"
            >提交到数据库</a-button
          >
        </template>
        </a-modal>

    </div>
</template>

<script>
import touxiang1 from "assets/touxiang1.jpg";

import { getUserName } from "../common/env";
import SentenceService from "@/service/sentence_service";
const sentenceService = new SentenceService();
const columns = [
  {
    //title: "名称",
    dataIndex: "name",
    key: "name",
    slots: { title: "customTitle" },
    scopedSlots: { customRender: "name" },
    align: "center",
    width: 100,
    filters: [
      { text: "循环型", value: "循环型" },
      { text: "定时型", value: "定时型" },
      { text: "监听型", value: "监听型" },
      { text: "条件型", value: "条件型" },
      { text: "赋值型", value: "赋值型" },
      { text: "绑定型", value: "绑定型" },
      { text: "保存型", value: "保存型" },
    ],
    onFilter: (value, record) => record.name.indexOf(value) === 0,
  },
  {
    title: "描述",
    dataIndex: "description",
    key: "description",
    align: "center",
  },

  {
    title: "例子",
    dataIndex: "example",
    key: "example",
    align: "center",
  },
  {
    title: "序列化表示",
    dataIndex: "serialization",
    key: "serialization",
    align: "center",
  },

  {
    title: "句型状态",
    dataIndex: "status",
    key: "status",
    align: "center",
    scopedSlots: { customRender: "sentenceStatus" },

  },
  {
    title: "类型",
    key: "type",
    dataIndex: "type",
    scopedSlots: { customRender: "type" },
    align: "center",
    filters: [
      { text: "用户自定义型", value: "2" },
      { text: "标准型", value: "1" },
    ],
    onFilter: (value, record) => record.type.indexOf(value) === 0,
    /* onFilter: (value, record) => {
        if(record.type!=null&&record.type.indexOf(value)>-1){
        record.type.indexOf(value) === 0
      }
        } 这个方法没用，过滤出来的是空的，没数据,加个return就有数据了*/
  },
  {
    title: "编辑者",
    key: "creatorName",
    dataIndex: "creatorName",
    scopedSlots: { customRender: "creatorName" },
    align: "center",
    width: 80,
  },
  /*  {
    title: "创建时间",
    key: "createTime",
    dataIndex: "createTime",
    align: "center",
    width: 120,
    sorter: (a, b) =>
      new Date(a.createTime).getTime() - new Date(b.createTime).getTime(),
   
  }, */
  {
    title: "修改时间",
    key: "updateTime",
    dataIndex: "updateTime",
    align: "center",
    width: 120,
    sorter: (a, b) =>
      new Date(a.updateTime).getTime() - new Date(b.updateTime).getTime(),
    //sortOrder: sortedInfo.columnKey === 'updateTime' && sortedInfo.order,
    // ellipsis: true,
  },

  {
    title: "操作",
    key: "action",
    scopedSlots: { customRender: "action" },
    align: "center",
    width: 80,
  },
];

const sentenceTamplate = JSON.stringify({
  name: "",
  description: "",
  //action: "",
  serialization: "",
  example: "",
  type: "1",
  status:0,
  creatorName: "",
  updateTime: "",
  creatorId: "",
  createTime: "",
});
export default{
    data(){
        return{
 blpLogo,
     touxiang1,

      userName: getUserName(),
  name: "SentencePage",
  components: {},
  
      searchName:"",
      searchType: "编辑者",
      sentenceNames: [
        { sentenceNameId: 1, name: "赋值型" },
        { sentenceNameId: 2, name: "循环型" },
        { sentenceNameId: 3, name: "监听型" },
        { sentenceNameId: 4, name: "定时型" },
        { sentenceNameId: 5, name: "条件型" },
        { sentenceNameId: 6, name: "绑定型" },
        { sentenceNameId: 7, name: "保存型" },
      ],
      sentenceData: [],
      //mySentenceData:[],
     
      columns,
      pagination: {
        page: 1,
        count: 10,
        total: 0,
      },
      tableLoading: false,
      data:[],
      searchStr: "",
      Visible: false,
      addSentenceVisible: false,
      confirmLoading: false,
      labelCol: { span: 6 },
      wrapperCol: { span: 12 },
      title: "",
      actionType: "ADD",
      form: JSON.parse(sentenceTamplate),
      rules: {
        name: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },
      sortedInfo: null,
        }
    },
    created() {this.refresh();},
  watch: {},
  computed: {},
  methods: {
 toHome() {
      this.$router.push({ name: "Home" });
    },

     refresh() {
      sentenceService.getAllSentence(
        {
          //type: 1,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);

            this.sentenceData = json.data.rows.sentence.sort((a, b) => {
                //根据词汇创建的先后顺序进行展示
                const date1 = new Date(a.updateTime).getTime();
                const date2 = new Date(b.updateTime).getTime();
                return date2 - date1;
              });;
          
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
          
        }
      );
    },
    refreshAllSentence(){
      sentenceService.getAllSentence(
        {
          //type: 1,
        },
        {
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
          
        }
      );
    },
    refreshMySentence(){
      sentenceService.getAllSentenceByCreatorName(
        {
          //creatorName: value.trim(),
          creatorName: '王哲',
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.sentenceData = json.data.rows.sentence;
           
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //路由内容切换
    changeMenu(route){
      console.log(route)
      //获取路由对象并切换
      this.$router.push(route);
      this.refreshMySentence();
            },
  
     toHome() {
      this.$router.push({ name: "Home" });
    },

    openAddSentence() {
      this.actionType = "ADD";
      this.title = "添加句型信息";
      this.addSentenceVisible = true;
      this.form = JSON.parse(sentenceTamplate);
    },

    openModifySentence(sentence) {
      this.actionType = "MODIFY";
      this.addSentenceVisible = true;
      this.title = "修改句型信息";
      this.form = sentence;
      let has =
        this.sentenceNames.findIndex(
          (t) => t.sentenceNameId == sentence.sentenceNameId
        ) > -1;

      if (!has) {
        this.sentenceNames.push({
          sentenceNameId: sentence.sentenceNameId,
          name: sentence.name,
        });
      }
    },
    async submit() {
      this.confirmLoading = true;

      try {
        let result;
        if (this.actionType == "ADD") {
        
          result = await sentenceService.createSentence(this.form);
          this.$message.success('添加句型成功');
        } else {
          result = await sentenceService.updateSentence(this.form);
          this.$message.success('修改句型成功');
        }

        this.confirmLoading = false;
        this.addSentenceVisible = false;
        this.refresh();
      } catch (msg) {
        this.confirmLoading = false;
        console.log(msg);
      }
    },
    async deleteSentence(sentence){
      await sentenceService.deleteSentence(sentence);
      this.$message.success('删除句型成功');
      //this.fetch(this.pagination);
      this.refresh();
    },
    async handleOk(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        }
        console.log("数据校验成功，字段都是合理的,可以提交保存了");
        this.submit();
      });
    },
    handleCancel() {
      this.addSentenceVisible = false;
      
    },
     async handleSaveToLocale(){
      let content = ""
     Object.entries( this.form).forEach(e => {
     // console.log(e);
      content += e[0] + ":" + e[1] + "\n";
     })

    this.doSave(content, "txt", this.form.name);
    },
    doSave(value, type, name) {
        var blob;
        if (typeof window.Blob == "function") {
          blob = new Blob([value], {
            type: type
          });
        } else {
          var BlobBuilder = window.BlobBuilder || window.MozBlobBuilder || window.WebKitBlobBuilder || window.MSBlobBuilder;
          var bb = new BlobBuilder();
          bb.append(value);
          blob = bb.getBlob(type);
        }
        var URL = window.URL || window.webkitURL;
        var bloburl = URL.createObjectURL(blob);
        var anchor = document.createElement("a");
        if ('download' in anchor) {
          anchor.style.visibility = "hidden";
          anchor.href = bloburl;
          anchor.download = name;
          document.body.appendChild(anchor);
          var evt = document.createEvent("MouseEvents");
          evt.initEvent("click", true, true);
          anchor.dispatchEvent(evt);
          document.body.removeChild(anchor);
        } else if (navigator.msSaveBlob) {
          navigator.msSaveBlob(blob, name);
        } else {
          location.href = bloburl;
        }

        this.addSentenceVisible = false;

      },


    handleTableChange(pagination, filters, sorter) {
      //<a-table>里面有一个 @change="handleTableChange"
      console.log('这是pagination的内容');
      console.log(pagination);
      console.log('这是filters的内容');
      console.log(filters);
       console.log('这是sorter的内容');
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
      
      const result = await sentenceService.getAllSentence();//返回结果是一个Model
      console.log("帮语句型工者执行了fetch方法");
      console.log(result);
      this.data = result.rows;
      this.pagination.total = result.total;
      this.tableLoading = false;
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
    },
  callback(key){
    if (key == 1) {
          this.refreshAllSentence();
        } else if (key == 2) {
          this.refreshMySentence();
        }
  },
  //按时间对数组mySentenceData进行排序,得到的新数组是按最新的日期从上到下排列的
  sortData(a, b){
        return new Date(b.updateTime).getTime() - new Date(a.updateTime).getTime()
    }

  },
}
</script>
<style lang="scss" scoped>

</style>>