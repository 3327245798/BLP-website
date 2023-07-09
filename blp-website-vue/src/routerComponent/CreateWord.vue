<template>
  <!-- <div>这里可以创建词汇</div> -->
  <div>
    <div class="button-nav">
      <a-button type="primary" @click="openAddWord()"> 新增词汇 </a-button>
      <a-input-group class="inputGroup">
        <a-select
          default-value="编辑者"
          v-model="searchType"
          style="width: 140px"
        >
          <a-select-option value="编辑者"> 编辑者 </a-select-option>
          <a-select-option value="词汇状态"> 词汇状态 </a-select-option>
        </a-select>
        <a-input-search
          placeholder="根据条件进行搜索"
          enter-button
          v-model="searchName"
          @search="onSearch"
        >
        </a-input-search>
        <span
          style="margin-left: 30px; background: white; border-radius: 50%"
          @click="refresh()"
          ><a-icon type="reload"
        /></span>
      </a-input-group>
    </div>
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
              // message: '词汇名字是必填的,请输入',
              trigger: 'blur',
            },
          ]"
        >
          <a-input placeholder="请输入词汇名称" v-model="wordForm.name" />
        </a-form-model-item>
        <a-form-model-item
          label="描述"
          prop="description"
          :rules="[
            {
              required: true,
              // message: '词汇是必填的,请输入',
              trigger: 'blur',
            },
          ]"
        >
          <a-textarea
            placeholder="请输入词汇描述"
            v-model="wordForm.description"
          />
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
      <span slot="customUpdateTimeTitle">
        修改时间
        <a-dropdown>
          <a-menu slot="overlay" @click="onDatePickClick">
            <a-menu-item key="1"> 一个月内 </a-menu-item>
            <a-menu-item key="2"> 一周内</a-menu-item>
            <a-menu-item key="3"> 今天</a-menu-item>
          </a-menu>
          <a> <a-icon type="down" /> </a>
        </a-dropdown>
      </span>
      <!-- 0禁用 1：审查中2：未审查 3：已启用  -->
      <span slot="statu" slot-scope="text, record">
        <a-tag v-if="(text == '1')" color="purple">审核中</a-tag>
        <a-tag v-else-if="(text == '2')" color="green">未审核</a-tag>
        <a-tag v-else-if="(text == '3')" color="orange">已启用</a-tag>
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
        <!--  <a-divider type="vertical" />
        <a-button type="primary" size="small" ghost> 固件 </a-button> -->
      </span>
       
      <div
       
        slot="expandedRowRender"
        slot-scope="record, index"
        style="margin: 0"
      >
      {{record.firmwareId}}
        <a-button
          class="a"
          type="primary"
          @click="openAddUnit(record.id, record, index)"
        >
          绑定固件
        </a-button>
        <!-- 判断该词汇是值类型还是固件类型还是webservice类型，如果是值类型就只展示 -->
        {{record}}
        <a-table
          v-if="record.type!=0"
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
          <span slot="unitCode" slot-scope="text" class="table-operation">
            <a-button
              type="primary"
              size="small"
              @click="handleSeeCode(record)"
            >
              查看
            </a-button>
          </span>
          <div
            slot="expandedRowRender"
            slot-scope="record"
            style="display: flex; margin-right: 30px"
          >
            <div class="inputParam">
              <h2>输入参数:</h2>
              <div v-for="(param, index) in record.inputParams" :key="index">
                <span
                  ><a-tag color="red">{{ param.name }}</a-tag></span
                >-
                <span
                  ><a-tag color="blue">{{ param.cnName }}</a-tag></span
                >-
                <!-- 参数本身类型 1、int  ， 2、float，3、String ，4、Date  5、boolean 、 6、object 、7 list -->
                <span v-if="param.objType == '1'"
                  ><a-tag color="orange">int</a-tag></span
                >

                <span v-else-if="param.objType == '2'"
                  ><a-tag color="orange">float</a-tag></span
                >
                <span v-else-if="param.objType == '3'"
                  ><a-tag color="orange">String</a-tag></span
                >
                <span v-else-if="param.objType == '4'"
                  ><a-tag color="orange">Date</a-tag></span
                >
                <span v-else-if="param.objType == '5'"
                  ><a-tag color="orange">boolean</a-tag></span
                >
                <span v-else-if="param.objType == '6'"
                  ><a-tag color="orange">object</a-tag></span
                >
                <span v-else-if="param.objType == '7'"
                  ><a-tag color="orange">list</a-tag></span
                >
              </div>
            </div>
            <a-divider type="vertical" />
            <div class="outputParam">
              <h2>输出参数:</h2>
              <div v-for="(param, index) in record.outputParams" :key="index">
                <span
                  ><a-tag color="red">{{ param.name }}</a-tag></span
                >-
                <span
                  ><a-tag color="blue">{{ param.cnName }}</a-tag></span
                >-
                <!-- 参数本身类型 1、int  ， 2、float，3、String ，4、Date  5、boolean 、 6、object 、7 list -->
                <span v-if="param.objType == '1'"
                  ><a-tag color="orange">int</a-tag></span
                >

                <span v-else-if="param.objType == '2'"
                  ><a-tag color="orange">float</a-tag></span
                >
                <span v-else-if="param.objType == '3'"
                  ><a-tag color="orange">String</a-tag></span
                >
                <span v-else-if="param.objType == '4'"
                  ><a-tag color="orange">Date</a-tag></span
                >
                <span v-else-if="param.objType == '5'"
                  ><a-tag color="orange">boolean</a-tag></span
                >
                <span v-else-if="param.objType == '6'"
                  ><a-tag color="orange">object</a-tag></span
                >
                <span v-else-if="param.objType == '7'"
                  ><a-tag color="orange">list</a-tag></span
                >
              </div>
            </div>
          </div>
          <span slot="operation" slot-scope="text" class="table-operation">
            <a>取消绑定</a>
          </span>
        </a-table>

      </div>
    </a-table>
    <a-modal
      title="查看代码"
      :visible="seeCodeVisible"
      :maskStyle="{ backgroundColor: 'rgba(0,0,0,.45)' }"
      :footer="null"
      @cancel="handleSeeCodeCancel"
    >
      <p style="color: blue; font-weight: bold">{{ codeMeaning }}</p>
    </a-modal>
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
        <a-form-item label="固件类型">
          <!-- 固件类型，(词汇绑定时可以进行选择)目前设计了五种，值0、工具部件1、页面2、web service3、微服务4，用01234分别表示 -->
          <a-radio-group
            name="unitTypeGroup"
            v-model="unitForm.type"
            @change="onUnitTypeChange"
            :default-value="0"
          >
            <a-radio value="0"> 值 </a-radio>
            <a-radio value="1"> 部件 </a-radio>
            <a-radio value="3"> WebService或微服务 </a-radio>
          </a-radio-group>
        </a-form-item>
        <div v-if="unitForm.type == '1'">
          <a-form-model-item label="工具" prop="toolId">
            <a-select
              show-search
              @change="handleSelectToolChange"
              :default-active-first-option="false"
              :show-arrow="true"
              :not-found-content="null"
              placeholder="选择或搜索工具"
              @select="getFirmwaresByToolId"
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
          <a-form-model-item label="固件" prop="toolId">
            <a-select
              v-model="unitForm.firmwareId"
              show-search
              @select="getParamByFirmwareAndInOut"
              :default-active-first-option="false"
              :show-arrow="true"
              :not-found-content="null"
              placeholder="选择或搜索固件"
            >
              <a-select-option
                :key="f.id"
                :value="f.id"
                v-for="f in firmwareList"
              >
                {{ f.firmwareName }}
              </a-select-option>
            </a-select>
          </a-form-model-item>

          <a-form-model-item label="输入参数">
            <!-- <a-textarea v-model="inputParams">{{inputParams}}<a-textarea /> -->

            <span v-for="(inp, index) in inputParams" :key="index">
              <a-tag v-if="index == 0" color="magenta">{{ inp.cnName }}</a-tag>
              <a-tag v-else-if="index == 1" color="volcano">{{
                inp.cnName
              }}</a-tag>
              <a-tag v-else-if="index == 2" color="orange">{{
                inp.cnName
              }}</a-tag>
              <a-tag v-else-if="index == 3" color="gold">{{
                inp.cnName
              }}</a-tag>
              <a-tag v-else-if="index == 4" color="lime">{{
                inp.cnName
              }}</a-tag>
              <a-tag v-else-if="index == 5" color="blue">{{
                inp.cnName
              }}</a-tag>
              <a-tag v-else-if="index == 6" color="green">{{
                inp.cnName
              }}</a-tag>
              <a-tag v-else-if="index == 7" color="cyan">{{
                inp.cnName
              }}</a-tag>
            </span>
          </a-form-model-item>
          <a-form-model-item label="输出参数">
            <!-- <a-textarea v-model=" outputParams">{{ outputParams}}<a-textarea /> -->
            <!-- <a-textarea v-model=" outputParams"/> -->
            <span v-for="(op, index) in outputParams" :key="index">
              <a-tag v-if="index == 0" color="magenta">{{ op.cnName }}</a-tag>
              <a-tag v-else-if="index == 1" color="volcano">{{
                op.cnName
              }}</a-tag>
              <a-tag v-else-if="index == 2" color="orange">{{
                op.cnName
              }}</a-tag>
              <a-tag v-else-if="index == 3" color="gold">{{ op.cnName }}</a-tag>
              <a-tag v-else-if="index == 4" color="lime">{{ op.cnName }}</a-tag>
              <a-tag v-else-if="index == 5" color="blue">{{ op.cnName }}</a-tag>
              <a-tag v-else-if="index == 6" color="green">{{
                op.cnName
              }}</a-tag>
              <a-tag v-else-if="index == 7" color="cyan">{{ op.cnName }}</a-tag>
            </span>
          </a-form-model-item>
           <a-form-model-item label="固件代码" prop="meaning">
            <a-textarea
              v-model="unitForm.meaning"
              placeholder="请输入工具部件的固件代码"
              :auto-size="{ minRows: 2, maxRows: 12 }"
            />
          </a-form-model-item>
        </div>
        <div v-else-if="unitForm.type == '0'">
          <a-form-model-item label="固件" prop="meaning">
            <a-textarea
              v-model="unitForm.meaning"
              placeholder="对于值类型的这种固件,你可以出入一个url连接或者id,根据你实际的开发需要填写即可"
            />
          </a-form-model-item>
        </div>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { getUserName } from "../common/env";
import WordService from "@/service/word_service";
import BandService from "@/service/band_service";
import UnitService from "@/service/unit_service";
import FirmwareService from "@/service/firmware_service";
import ParamService from "@/service/param_service";

const wordService = new WordService();
const bandService = new BandService();
const unitService = new UnitService();
const firmwareService = new FirmwareService();
const paramService = new ParamService();

const wordColumns = [
  { title: "词汇", dataIndex: "name", key: "name", width: 120 },
  { title: "描述", dataIndex: "description", key: "description", width: 160 },
  { title: "编辑者", dataIndex: "creatorName", key: "creatorName", width: 80 },
  {
    title: "状态",
    dataIndex: "status",
    key: "status",
    scopedSlots: { customRender: "statu" },
    width: 60,
  },
  /* {
    title: "词汇语义",
    dataIndex: "meaning",
    key: "meaning",
    width: 120,
  }, */
  {
    slots: { title: "customUpdateTimeTitle" },
    dataIndex: "updateTime",
    key: "updateTime",
    width: 130,
  },
  {
    title: "操作",
    dataIndex: "action",
    key: "x",
    scopedSlots: { customRender: "action" },
    width: 200,
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
  meaning: "",
  update_time: "",
  status: 0,
  description: "",
  firmwareId: 0,
});
const innerUnitColumns = [
  { title: "工具", dataIndex: "toolName", key: "toolName" },
  { title: "工具id", dataIndex: "toolId", key: "toolId" },

  { title: "固件别名", dataIndex: "firmwareName", key: "firmwareName" },
  //{ title: "固件名", dataIndex: "actionApi", key: "actionApi" },
  {
    title: "固件代码",
    dataIndex: "",
    key: "y",
    scopedSlots: { customRender: "unitCode" },
  },
  //{ title: "输入参数", dataIndex: "inputParams", key: "inputParams",width:200, scopedSlots: { customRender: "InputParamInformation" },},
  //{ title: "输出参数", dataIndex: "outputParams", key: "outputParams" , scopedSlots: { customRender: "OutputParamInformation" },},
  // { title: "参数实体", dataIndex: "clazz", key: "clazz" },
  /*  {
    title: "操作",
    dataIndex: "",
    key: "x",
    scopedSlots: { customRender: "operation" },
  }, */
];
const innerUnitData = [];
const unitTamplate = JSON.stringify({
  meaning: "",
  type: 0,
  firmwareId: "",
});
export default {
  data() {
    return {
      searchType: "编辑者",
      wordData,
      wordColumns,
      innerUnitColumns,
      innerUnitData,
      selectReord: null,
      unitForm: JSON.parse(unitTamplate),
      curentEditRecordIndex: null,
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
      tools: [], //工具列表
      firmwareList: [], //固件列表
      inputParams: [], //输入参数列表inputParams outputParams
      outputParams: [], //输出参数列表

      seeCodeVisible: false, //控制 查看某个词汇对应的固件代码 的modal是否可见

      codeMeaning: "",
    };
  },
  created() {
    this.refresh();
  },
  watch: {},
  computed: {},
  methods: {
    onUnitTypeChange(e) {
      console.log("选中了", e.target.value);
      this.unitForm.type = e.target.value;
    },
    //选中工具下拉框触发，这样可以获取到工具的id，以便于获取该工具的部件，为部件下拉框服务
    getFirmwaresByToolId(value) {
      console.log(value);
      firmwareService.getFirmwaresByToolId(
        {
          toolId: value,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.firmwareList = json.data.rows.firmware;
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //选中部件时调用，这样可以获取到固件的id,以便于获取该固件的输入数参数，为输入参数及输出参数文本域服务

    getParamByFirmwareAndInOut(value) {
      console.log(value);
      //调用两个方法，将输入输出参数都获取到
      this.getInputParamByFirmwareId(value);
      this.getOutputParamByFirmwareId(value);
    },
    //传type=1,根据固件id获取输入参数
    getInputParamByFirmwareId(value) {
      paramService.getParamsByFirmwareIdAndType(
        {
          firmwareId: value,
          type: 1,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.inputParams = json.data.rows.paramList;
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //传type=2,根据固件id获取固件输出参数
    getOutputParamByFirmwareId(value) {
      console.log(value);
      paramService.getParamsByFirmwareIdAndType(
        {
          firmwareId: value,
          type: 2,
        },
        {
          onSuccess: (model, fModel, json) => {
            this.outputParams = json.data.rows.paramList;
            console.log(json);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },

    refresh() {
      //获取所有的词汇，已经绑定固件的和未绑定固件的词汇都会获取到
      wordService.getAllPublicWordNoUnit({
        onSuccess: (model, fModel, json) => {
          console.log(json);
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
    //
    //根据词汇id获取参数信息，固件信息
    getToolInfoFirmwareInfoParamInfoByWordId(recordId, record) {
      console.log("获取已绑定固件的词汇");
      console.log(this.wordData);
      firmwareService.getToolInfoFirmwareInfoParamInfoByWordId(
        {
          wordId: recordId,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            if (json.data.rows.firmware) {
              this.$set(record, "unitData", [json.data.rows.firmware]);
              //this.$set(record, "unitData.meaning", record.meaning);
            }
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

          let sameWord = this.wordData.some((item) => {
            return item.name == this.wordForm.name;
          });

          if (sameWord) {
            alert("已经存在该词汇，不予许重复添加");
            this.confirmLoading = false;
            return;
          }
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
      this.wordForm = JSON.parse(wordTamplate);//将表单置空
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
      if (this.unitForm.type == "1") {
        //提交类型为工具部件的固件
        let index = this.tools.findIndex(
          (t) => t.toolId == this.unitForm.toolId
        );
        this.unitForm.toolName = this.tools[index].toolName;
      }

      try {
        let result;
        if (this.actionType == "ADD") {
          result = await wordService.updateWord(this.unitForm);
          console.log('提交绑定固件实际上是修改词汇表ADD',this.unitForm);
          this.$message.success("绑定固件成功newWordFrom");
        } else {
          result = await unitService.updateBindUnit(this.unitForm);
          this.$message.success("修改固件成功");
        }

        this.confirmLoading = false;
        this.addUnitVisible = false;
        this.getToolInfoFirmwareInfoParamInfoByWordId(
          this.wordData[this.curentEditRecordIndex].id,
          this.wordData[this.curentEditRecordIndex]
        );
      } catch (msg) {
        this.confirmLoading = false;
      }
    },
    openAddUnit(id, record, index) {
      this.selectReord = record;
      this.actionType = "ADD";
      this.addUnitVisible = true;
      this.unitFormTitle = record.name + "=》绑定固件";
      this.unitForm = JSON.parse(unitTamplate);
      this.unitForm.wordId = id;
      this.curentEditRecordIndex = index;
      this.unitForm = Object.assign(this.unitForm, record);
      console.log('选择固件类型后的词汇',this.unitForm)
      console.log(index);
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
    //查看代码
    handleSeeCode(record) {
      console.log("执行了查看代码的弹窗这个方法");
      console.log(record.id);
      this.seeCodeVisible = true;
      this.codeMeaning = record.meaning;
    },

    //查看词汇代码modal关闭
    handleSeeCodeCancel(record) {
      this.seeCodeVisible = false;
      this.codeMeaning = "";
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
      this.getToolInfoFirmwareInfoParamInfoByWordId(record.id, record);
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
    onSearch() {
      if (this.searchType == "编辑者") {
        this.onSearchByCreatorName(this.searchName);
      } else {
        this.onSearchByUnitStatus(this.searchName);
      }
    },
    onSearchByCreatorName(value) {
      console.log(value);
      if (value == "") {
        this.refresh();
      } else {
        wordService.getAllWordsNoUnitByCreatorName(
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
    onSearchByUnitStatus(value) {
      console.log("根据词汇查询词汇" + value);
      if (value == "") {
        this.refresh();
      } else {
        let status = 0;
        //值、工具、页面、WebService、微服务
        if (value == "审核中") {
          status = 0;
        } else if (value == "启用中") {
          status = 1;
        } else if (value == "未通过") {
          status = 2;
        } else {
          status = 3;
        }
        wordService.getAllWordsNoUnitByStatus(
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
    onDatePickClick({ key }) {
      console.log(`点击了选项 ${key}`);
      console.log(`${key}`);
      let datePickValue = `${key}`;

      console.log("datePickValue是" + datePickValue);
      if (datePickValue == "2") {
        wordService.getAllWordsNoUnitInWeek({
          onSuccess: (model, fModel, json) => {
            console.log(json); //一个周内创建的词汇
            this.wordData = json.data.rows.word;
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        });
      } else if (datePickValue == "1") {
        wordService.getAllWordsNoUnitInMonth({
          onSuccess: (model, fModel, json) => {
            console.log(json); //一月内创建的词汇
            this.wordData = json.data.rows.word;
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        });
      } else if (datePickValue == "3") {
        wordService.getAllWordsNoUnitInDay({
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
.button-nav {
  display: flex;
}
.inputGroup {
  display: flex;
  width: 50%;
  margin-left: 20px;
  justify-content: center;
  align-items: center;
  //border:2px solid black;
}
</style>>