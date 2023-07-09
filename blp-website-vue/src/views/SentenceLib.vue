<template>
  <!-- 这里是句型库 -->
  <div class="total">
    <div class="header">
      <img :src="blpLogo" style="cursor: pointer" @click="toHome" />
      <div class="header-right">
        <div>
          <span @click="$router.push('websiteIndex')">论坛</span>
          <span @click="$router.push('businessTypeAppcase')">剧本案例</span>

          <span @click="$router.push('wordLib')">词汇库</span>
          <span @click="$router.push('sentenceLib')" style="color: purple"
            >句型库</span
          >
        </div>
        <div>
          <span @click="$router.push('talkWorkCenter')">创作中心</span>
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
        <a-tabs
          default-active-key="1"
          v-model="activeKey"
          @change="handleSentenceNameChange"
        >
          <a-tab-pane key="1">
            <span slot="tab">
              <sapn style="font-weight: bold">命令型</sapn>
              <a-tag :color="`${activeKey == 1 ? '#108ee9' : ''}`">
                {{ MinglingSentenceLength }}
              </a-tag>
            </span>
            <div
              style="
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <a-input-search
                placeholder="input search text"
                style="width: 400px; boder-raduis: 10px"
                v-model="userInputKeyword"
                @input="handleInput"
                @search="onSentenceSearch"
              >
                <a-icon
                  slot="suffix"
                  type="close-circle"
                  @click="clearInput(userInputKeyword)"
                  style="
                    color: rgba(0, 0, 0, 0.45);
                    margin-right: 5px;
                    cursor: pointer;
                  "
                />
              </a-input-search>
            </div>

            <a-table
              :columns="columns"
              :row-key="(record) => record.id"
              :pagination="pagination"
              :loading="tableLoading"
              :data-source="sentenceData"
              bordered
              @change="handleTableChange"
            >
              <!--  :customRow="demandList" 句型表格中，触发每一行的触发事件-->
              <a slot="name" slot-scope="text">{{ text }} </a>
              <div slot="unitTextSlot" slot-scope="text, record">
                <CopyCode :code="record.unitText"></CopyCode>
              </div>
              <span slot="types" slot-scope="types">
                <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
                <a-tag color="green" v-else-if="types == 2">
                  自定义句型库
                </a-tag>
                <a-tag color="red" v-else> 未知类型 </a-tag>
              </span>
              <span slot="customUpdateTimeTitle">
                <a-dropdown>
                  <a-menu slot="overlay" @click="onDatePickClick">
                    <a-menu-item key="1"> 一个月内 </a-menu-item>
                    <a-menu-item key="2"> 一周内</a-menu-item>
                    <a-menu-item key="3"> 今天</a-menu-item>
                  </a-menu>
                  <a> 创建时间<a-icon type="down" /> </a>
                </a-dropdown>
              </span>
              <span slot="statu" slot-scope="statu">
                <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
                <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
                <a-tag color="orange" v-else-if="statu == 2">
                  审核通过开发中
                </a-tag>
                <a-tag color="green" v-else-if="statu == 3">
                  开发完成启用中
                </a-tag>
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
                  <a-button type="primary" size="small" ghost>
                    审核通过请开发
                  </a-button>
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
                  <a-button type="danger" size="small" ghost>
                    审核不通过
                  </a-button>
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
          </a-tab-pane>
          <a-tab-pane key="2">
            <span slot="tab">
              <sapn style="font-weight: bold">赋值型</sapn>
              <a-tag :color="`${activeKey == 2 ? '#108ee9' : ''}`">
                {{ FuZhiSentenceLength }}
              </a-tag>
            </span>
            <div
              style="
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <a-input-search
                placeholder="input search text"
                style="width: 400px; boder-raduis: 10px"
                v-model="userInputKeyword"
                @input="handleInput"
                @search="onSentenceSearch"
              />
            </div>
            <a-table
              :columns="columns"
              :row-key="(record) => record.id"
              :pagination="pagination"
              :loading="tableLoading"
              :data-source="sentenceData"
              bordered
              @change="handleTableChange"
              :customRow="demandList"
            >
              <a slot="name" slot-scope="text">{{ text }} </a>
              <span slot="unitTextSlot" slot-scope="text">
                <CopyCode :code="text"></CopyCode>
              </span>
              <span slot="types" slot-scope="types">
                <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
                <a-tag color="green" v-else-if="types == 2">
                  自定义句型库
                </a-tag>
                <a-tag color="red" v-else> 未知类型 </a-tag>
              </span>
              <span slot="customUpdateTimeTitle">
                <a-dropdown>
                  <a-menu slot="overlay" @click="onDatePickClick">
                    <a-menu-item key="1"> 一个月内 </a-menu-item>
                    <a-menu-item key="2"> 一周内</a-menu-item>
                    <a-menu-item key="3"> 今天</a-menu-item>
                  </a-menu>
                  <a> 创建时间<a-icon type="down" /> </a>
                </a-dropdown>
              </span>
              <span slot="statu" slot-scope="statu">
                <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
                <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
                <a-tag color="orange" v-else-if="statu == 2">
                  审核通过开发中
                </a-tag>
                <a-tag color="green" v-else-if="statu == 3">
                  开发完成启用中
                </a-tag>
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
                  <a-button type="primary" size="small" ghost>
                    审核通过请开发
                  </a-button>
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
                  <a-button type="danger" size="small" ghost>
                    审核不通过
                  </a-button>
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
          </a-tab-pane>

          <a-tab-pane key="3">
            <span slot="tab">
              <sapn style="font-weight: bold">计算型</sapn
              ><!-- 将消息型改为计算型 -->
              <a-tag :color="`${activeKey == 3 ? '#108ee9' : ''}`">
                {{ MessageSentenceLength }}
              </a-tag>
            </span>
            <div
              style="
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <a-input-search
                placeholder="input search text"
                style="width: 400px; boder-raduis: 10px"
                v-model="userInputKeyword"
                @input="handleInput"
                @search="onSentenceSearch"
              />
            </div>
            <a-table
              :columns="columns"
              :row-key="(record) => record.id"
              :pagination="pagination"
              :loading="tableLoading"
              :data-source="sentenceData"
              bordered
              @change="handleTableChange"
              :customRow="demandList"
            >
              <a slot="name" slot-scope="text">{{ text }} </a>
              <span slot="unitTextSlot" slot-scope="text, record">
                <CopyCode :code="text"></CopyCode>
              </span>
              <span slot="types" slot-scope="types">
                <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
                <a-tag color="green" v-else-if="types == 2">
                  自定义句型库
                </a-tag>
                <a-tag color="red" v-else> 未知类型 </a-tag>
              </span>
              <span slot="customUpdateTimeTitle">
                <a-dropdown>
                  <a-menu slot="overlay" @click="onDatePickClick">
                    <a-menu-item key="1"> 一个月内 </a-menu-item>
                    <a-menu-item key="2"> 一周内</a-menu-item>
                    <a-menu-item key="3"> 今天</a-menu-item>
                  </a-menu>
                  <a> 创建时间<a-icon type="down" /> </a>
                </a-dropdown>
              </span>
              <span slot="statu" slot-scope="statu">
                <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
                <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
                <a-tag color="orange" v-else-if="statu == 2">
                  审核通过开发中
                </a-tag>
                <a-tag color="green" v-else-if="statu == 3">
                  开发完成启用中
                </a-tag>
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
                  <a-button type="primary" size="small" ghost>
                    审核通过请开发
                  </a-button>
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
                  <a-button type="danger" size="small" ghost>
                    审核不通过
                  </a-button>
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
          </a-tab-pane>
          <a-tab-pane key="4">
            <span slot="tab">
              <sapn style="font-weight: bold">授权型</sapn>
              <a-tag :color="`${activeKey == 4 ? '#108ee9' : ''}`">
                {{ AuthenSentenceLength }}
              </a-tag>
            </span>
            <div
              style="
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <a-input-search
                placeholder="input search text"
                style="width: 400px; boder-raduis: 10px"
                v-model="userInputKeyword"
                @input="handleInput"
                @search="onSentenceSearch"
              />
            </div>
            <a-table
              :columns="columns"
              :row-key="(record) => record.id"
              :pagination="pagination"
              :loading="tableLoading"
              :data-source="sentenceData"
              bordered
              @change="handleTableChange"
              :customRow="demandList"
            >
              <a slot="name" slot-scope="text">{{ text }} </a>
              <span slot="unitTextSlot" slot-scope="text, record">
                <CopyCode :code="text"></CopyCode>
              </span>
              <span slot="types" slot-scope="types">
                <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
                <a-tag color="green" v-else-if="types == 2">
                  自定义句型库
                </a-tag>
                <a-tag color="red" v-else> 未知类型 </a-tag>
              </span>
              <span slot="customUpdateTimeTitle">
                <a-dropdown>
                  <a-menu slot="overlay" @click="onDatePickClick">
                    <a-menu-item key="1"> 一个月内 </a-menu-item>
                    <a-menu-item key="2"> 一周内</a-menu-item>
                    <a-menu-item key="3"> 今天</a-menu-item>
                  </a-menu>
                  <a> 创建时间<a-icon type="down" /> </a>
                </a-dropdown>
              </span>
              <span slot="statu" slot-scope="statu">
                <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
                <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
                <a-tag color="orange" v-else-if="statu == 2">
                  审核通过开发中
                </a-tag>
                <a-tag color="green" v-else-if="statu == 3">
                  开发完成启用中
                </a-tag>
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
                  <a-button type="primary" size="small" ghost>
                    审核通过请开发
                  </a-button>
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
                  <a-button type="danger" size="small" ghost>
                    审核不通过
                  </a-button>
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
          </a-tab-pane>
          <a-tab-pane key="5">
            <span slot="tab">
              <sapn style="font-weight: bold">定时型</sapn>
              <a-tag :color="`${activeKey == 5 ? '#108ee9' : ''}`">
                {{ DingShiSentenceLength }}
              </a-tag>
            </span>
            <div
              style="
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <a-input-search
                placeholder="input search text"
                style="width: 400px; boder-raduis: 5px"
                v-model="userInputKeyword"
                @input="handleInput"
                @search="onSentenceSearch"
              />
            </div>
            <a-table
              :columns="columns"
              :row-key="(record) => record.id"
              :pagination="pagination"
              :loading="tableLoading"
              :data-source="sentenceData"
              bordered
              @change="handleTableChange"
              :customRow="demandList"
            >
              <a slot="name" slot-scope="text">{{ text }} </a>
              <span slot="unitTextSlot" slot-scope="text, record">
                <CopyCode :code="text"></CopyCode>
              </span>
              <span slot="types" slot-scope="types">
                <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
                <a-tag color="green" v-else-if="types == 2">
                  自定义句型库
                </a-tag>
                <a-tag color="red" v-else> 未知类型 </a-tag>
              </span>
              <span slot="customUpdateTimeTitle">
                <a-dropdown>
                  <a-menu slot="overlay" @click="onDatePickClick">
                    <a-menu-item key="1"> 一个月内 </a-menu-item>
                    <a-menu-item key="2"> 一周内</a-menu-item>
                    <a-menu-item key="3"> 今天</a-menu-item>
                  </a-menu>
                  <a> 创建时间<a-icon type="down" /> </a>
                </a-dropdown>
              </span>
              <span slot="statu" slot-scope="statu">
                <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
                <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
                <a-tag color="orange" v-else-if="statu == 2">
                  审核通过开发中
                </a-tag>
                <a-tag color="green" v-else-if="statu == 3">
                  开发完成启用中
                </a-tag>
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
                  <a-button type="primary" size="small" ghost>
                    审核通过请开发
                  </a-button>
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
                  <a-button type="danger" size="small" ghost>
                    审核不通过
                  </a-button>
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
          </a-tab-pane>
          <a-tab-pane key="6">
            <span slot="tab">
              <sapn style="font-weight: bold">延时型</sapn>
              <a-tag :color="`${activeKey == 6 ? '#108ee9' : ''}`">
                {{ DelaySentenceLength }}
              </a-tag>
            </span>
            <div
              style="
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <a-input-search
                placeholder="input search text"
                style="width: 400px; boder-raduis: 10px"
                v-model="userInputKeyword"
                @input="handleInput"
                @search="onSentenceSearch"
              />
            </div>
            <a-table
              :columns="columns"
              :row-key="(record) => record.id"
              :pagination="pagination"
              :loading="tableLoading"
              :data-source="sentenceData"
              bordered
              @change="handleTableChange"
              :customRow="demandList"
            >
              <a slot="name" slot-scope="text">{{ text }} </a>
              <span slot="unitTextSlot" slot-scope="text, record">
                <CopyCode :code="text"></CopyCode>
              </span>
              <span slot="types" slot-scope="types">
                <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
                <a-tag color="green" v-else-if="types == 2">
                  自定义句型库
                </a-tag>
                <a-tag color="red" v-else> 未知类型 </a-tag>
              </span>
              <span slot="customUpdateTimeTitle">
                <a-dropdown>
                  <a-menu slot="overlay" @click="onDatePickClick">
                    <a-menu-item key="1"> 一个月内 </a-menu-item>
                    <a-menu-item key="2"> 一周内</a-menu-item>
                    <a-menu-item key="3"> 今天</a-menu-item>
                  </a-menu>
                  <a> 创建时间<a-icon type="down" /> </a>
                </a-dropdown>
              </span>
              <span slot="statu" slot-scope="statu">
                <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
                <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
                <a-tag color="orange" v-else-if="statu == 2">
                  审核通过开发中
                </a-tag>
                <a-tag color="green" v-else-if="statu == 3">
                  开发完成启用中
                </a-tag>
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
                  <a-button type="primary" size="small" ghost>
                    审核通过请开发
                  </a-button>
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
                  <a-button type="danger" size="small" ghost>
                    审核不通过
                  </a-button>
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
          </a-tab-pane>
          <a-tab-pane key="7">
            <span slot="tab">
              <sapn style="font-weight: bold">条件型</sapn>
              <a-tag :color="`${activeKey == 7 ? '#108ee9' : ''}`">
                {{ ConditionSentenceLength }}
              </a-tag>
            </span>
            <div
              style="
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <a-input-search
                placeholder="input search text"
                style="width: 400px; boder-raduis: 10px"
                v-model="userInputKeyword"
                @input="handleInput"
                @search="onSentenceSearch"
              />
            </div>
            <a-table
              :columns="columns"
              :row-key="(record) => record.id"
              :pagination="pagination"
              :loading="tableLoading"
              :data-source="sentenceData"
              bordered
              @change="handleTableChange"
              :customRow="demandList"
            >
              <a slot="name" slot-scope="text">{{ text }} </a>
              <span slot="unitTextSlot" slot-scope="text, record">
                <CopyCode :code="text"></CopyCode>
              </span>
              <span slot="types" slot-scope="types">
                <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
                <a-tag color="green" v-else-if="types == 2">
                  自定义句型库
                </a-tag>
                <a-tag color="red" v-else> 未知类型 </a-tag>
              </span>
              <span slot="customUpdateTimeTitle">
                <a-dropdown>
                  <a-menu slot="overlay" @click="onDatePickClick">
                    <a-menu-item key="1"> 一个月内 </a-menu-item>
                    <a-menu-item key="2"> 一周内</a-menu-item>
                    <a-menu-item key="3"> 今天</a-menu-item>
                  </a-menu>
                  <a> 创建时间<a-icon type="down" /> </a>
                </a-dropdown>
              </span>
              <span slot="statu" slot-scope="statu">
                <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
                <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
                <a-tag color="orange" v-else-if="statu == 2">
                  审核通过开发中
                </a-tag>
                <a-tag color="green" v-else-if="statu == 3">
                  开发完成启用中
                </a-tag>
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
                  <a-button type="primary" size="small" ghost>
                    审核通过请开发
                  </a-button>
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
                  <a-button type="danger" size="small" ghost>
                    审核不通过
                  </a-button>
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
          </a-tab-pane>
          <a-tab-pane key="8">
            <span slot="tab">
              <sapn style="font-weight: bold"> 监听型</sapn>
              <a-tag :color="`${activeKey == 8 ? '#108ee9' : ''}`">
                {{ MonitorSentenceLength }}
              </a-tag>
            </span>
            <div
              style="
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <a-input-search
                placeholder="input search text"
                style="width: 400px; boder-raduis: 10px"
                v-model="userInputKeyword"
                @input="handleInput"
                @search="onSentenceSearch"
              />
            </div>
            <a-table
              :columns="columns"
              :row-key="(record) => record.id"
              :pagination="pagination"
              :loading="tableLoading"
              :data-source="sentenceData"
              bordered
              @change="handleTableChange"
              :customRow="demandList"
            >
              <a slot="name" slot-scope="text">{{ text }} </a>
              <span slot="unitTextSlot" slot-scope="text, record">
                <!-- <span v-if="text"> {{text.substring(0,40)}}</span>
        <a-dropdown v-if="text&&text.length>9" >
          
    <a   @click="showUnitCode(record)">
      ...更多
       <a-icon type="down" />
    </a>
    </a-dropdown> -->
                <span>{{ text.substring(0, 40) }}</span>
                <CopyCode :code="text"></CopyCode>
              </span>
              <span slot="types" slot-scope="types">
                <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
                <a-tag color="green" v-else-if="types == 2">
                  自定义句型库
                </a-tag>
                <a-tag color="red" v-else> 未知类型 </a-tag>
              </span>
              <span slot="customUpdateTimeTitle">
                <a-dropdown>
                  <a-menu slot="overlay" @click="onDatePickClick">
                    <a-menu-item key="1"> 一个月内 </a-menu-item>
                    <a-menu-item key="2"> 一周内</a-menu-item>
                    <a-menu-item key="3"> 今天</a-menu-item>
                  </a-menu>
                  <a> 创建时间<a-icon type="down" /> </a>
                </a-dropdown>
              </span>
              <span slot="statu" slot-scope="statu">
                <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
                <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
                <a-tag color="orange" v-else-if="statu == 2">
                  审核通过开发中
                </a-tag>
                <a-tag color="green" v-else-if="statu == 3">
                  开发完成启用中
                </a-tag>
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
                  <a-button type="primary" size="small" ghost>
                    审核通过请开发
                  </a-button>
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
                  <a-button type="danger" size="small" ghost>
                    审核不通过
                  </a-button>
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
          </a-tab-pane>
          <a-tab-pane key="9">
            <span slot="tab">
              <sapn style="font-weight: bold">循环型</sapn>
              <a-tag :color="`${activeKey == 9 ? '#108ee9' : ''}`">
                {{ LoopSentenceLength }}
              </a-tag>
            </span>
            <div
              style="
                display: flex;
                justify-content: center;
                align-items: center;
              "
            >
              <a-input-search
                placeholder="input search text"
                style="width: 400px; boder-raduis: 10px"
                v-model="userInputKeyword"
                @input="handleInput"
                @search="onSentenceSearch"
              />
            </div>
            <a-table
              :columns="columns"
              :row-key="(record) => record.id"
              :pagination="pagination"
              :loading="tableLoading"
              :data-source="sentenceData"
              bordered
              @change="handleTableChange"
              :customRow="demandList"
            >
              <a slot="name" slot-scope="text">{{ text }} </a>
              <span slot="unitTextSlot" slot-scope="text, record">
                <CopyCode :code="text"></CopyCode>
              </span>
              <span slot="types" slot-scope="types">
                <a-tag color="green" v-if="types == 1"> 标准型句型库 </a-tag>
                <a-tag color="green" v-else-if="types == 2">
                  自定义句型库
                </a-tag>
                <a-tag color="red" v-else> 未知类型 </a-tag>
              </span>
              <span slot="customUpdateTimeTitle">
                <a-dropdown>
                  <a-menu slot="overlay" @click="onDatePickClick">
                    <a-menu-item key="1"> 一个月内 </a-menu-item>
                    <a-menu-item key="2"> 一周内</a-menu-item>
                    <a-menu-item key="3"> 今天</a-menu-item>
                  </a-menu>
                  <a> 创建时间<a-icon type="down" /> </a>
                </a-dropdown>
              </span>
              <span slot="statu" slot-scope="statu">
                <a-tag color="red" v-if="statu == 0"> 审核中 </a-tag>
                <a-tag color="red" v-else-if="statu == 1"> 审核未通过 </a-tag>
                <a-tag color="orange" v-else-if="statu == 2">
                  审核通过开发中
                </a-tag>
                <a-tag color="green" v-else-if="statu == 3">
                  开发完成启用中
                </a-tag>
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
                  <a-button type="primary" size="small" ghost>
                    审核通过请开发
                  </a-button>
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
                  <a-button type="danger" size="small" ghost>
                    审核不通过
                  </a-button>
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
          </a-tab-pane>
        </a-tabs>
        <a-modal
          title="句型固件信息"
          :visible="sentenceInfoVisible"
          :confirm-loading="sentenceConfirmLoading"
          :footer="null"
          @cancel="handleCancel"
        >
          <p style="color: blue; font-weight: bold">句型：</p>
          <p>{{ description == null ? "无" : description }}</p>
          <p style="color: blue; font-weight: bold">固件：</p>
          <p>{{ unitText == null ? "无" : unitText }}</p>
          <p style="color: blue; font-weight: bold">句型描述及固件执行过程：</p>
          <p>{{ sentenceDescription == null ? "无" : sentenceDescription }}</p>
        </a-modal>
        <a-modal
          title="固件代码"
          :visible="unitCodeVisible"
          :footer="null"
          @cancel="handleUnitCodeCancel"
        >
          <p style="color: blue; font-weight: bold">{{ unitText }}</p>
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
import blpLogo from "assets/blpLogo1.png";

import touxiang1 from "assets/touxiang1.jpg";
import { getUserName } from "../common/env";
import CopyCode from "../myComponent/CopyCode.vue";
import MyComponent from "../myComponent/Test.vue";
import SentenceService from "@/service/sentence_service";
const sentenceService = new SentenceService();
const columns = [
  /* {
    title: "句型名字",
    dataIndex: "name",
    key: "name",
    width: 50,
    scopedSlots: { customRender: "name" },
    algin:'center',
  }, */
  { title: "句型", dataIndex: "description", key: "description", width: 120 },
  { title: "句型实例", dataIndex: "example", key: "example", width: 100 },
  // { title: "序列化表示", dataIndex: "serialization", key: "serialization" },
  {
    title: "固件",
    dataIndex: "unitText",
    key: "unitText",
    width: 100,
    //ellipsis:true,
    scopedSlots: { customRender: "unitTextSlot" },
  },
  /*  */
  {
    title: "编辑者",
    dataIndex: "creatorName",
    key: "creatorName",
    width: 60,
    algin: "center",
  },
  {
    //title: "创建时间",
    dataIndex: "updateTime",
    key: "updateTime",
    slots: { title: "customUpdateTimeTitle" },
    algin: "center",
    width: "120px",
  },
  /*  {
    title: "类型",
    dataIndex: "type",
    key: "type",
    scopedSlots: { customRender: "types" },
    algin:'center',
  }, */
  /* {
    title: "状态",
    dataIndex: "status",
    key: "status",
    scopedSlots: { customRender: "statu" },
    algin:'center',
    
  }, */
  /* {
    title: "操作",
    dataIndex: "",
    key: "x",
    scopedSlots: { customRender: "action" },
    width: 120,
    algin:'center',
  }, */
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
      blpLogo,
      touxiang1,

      userName: getUserName(),
      name: "SentencePage",

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
      activeKey: "1",
      //按照功能划分的8种句型的数量
      MinglingSentenceLength: 0,
      FuZhiSentenceLength: 0,
      AuthenSentenceLength: 0,
      MessageSentenceLength: 0,
      ConditionSentenceLength: 0,
      DelaySentenceLength: 0,
      LoopSentenceLength: 0,
      DingShiSentenceLength: 0,
      MonitorSentenceLength: 0,
      //控制句型的固件代码是否显示，是控制的固件这一列
      unitCodeVisible: false,
      //为table的每一行绑定了点击事件，当点击一行时，会弹出弹窗modal，显示1。句型2.固件代码，3.句型信息机固件执行过程
      sentenceInfoVisible: false,
      sentenceConfirmLoading: false,
      unitText: "这是弹出框内容",
      description: "句型",
      sentenceDescription: "句型及固件执行过程",
      userInputKeyword: "",
    };
  },
  created() {
    this.getMinglingSentenceList();
    this.getAllGongNengSentenceLength();
  },
  watch: {},
  computed: {},
  components: {
    MyComponent,
    CopyCode,
  },
  methods: {
    toHome() {
      this.$router.push({ name: "Home" });
    },
    /*获取8中类型句型（按照功能划分有8种）的数量，展示在tags标签页面*/
    getAllGongNengSentenceLength() {
      //1.命令型句型长度
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            let sentenceList = initialSentenceList.filter((item) => {
              return item.name == "1";
            });
            this.MinglingSentenceLength = sentenceList.length;
            console.log(this.MinglingSentenceLength);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      //2.赋值型句型长度
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            let sentenceList = initialSentenceList.filter((item) => {
              return item.name == "2";
            });
            this.FuZhiSentenceLength = sentenceList.length;
            console.log(this.FuZhiSentenceLength);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      //3.授权型句型长度
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            let sentenceList = initialSentenceList.filter((item) => {
              return item.name == "4";
            });
            this.AuthenSentenceLength = sentenceList.length;
            console.log(this.AuthenSentenceLength);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      //4.消息型句型长度
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            let sentenceList = initialSentenceList.filter((item) => {
              return item.name == "3";
            }); /* 消息型改为数字3 */
            this.MessageSentenceLength = sentenceList.length;
            console.log(this.MessageSentenceLength);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      //5.条件型句型长度
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            let sentenceList = initialSentenceList.filter((item) => {
              return item.name == "7";
            });
            this.ConditionSentenceLength = sentenceList.length;
            console.log(this.ConditionSentenceLength);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      //6.循环型句型长度
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            let sentenceList = initialSentenceList.filter((item) => {
              return item.name == "9";
            });
            this.LoopSentenceLength = sentenceList.length;
            console.log(this.LoopSentenceLength);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      //7.定时型句型长度
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            let sentenceList = initialSentenceList.filter((item) => {
              return item.name == "5";
            });
            this.DingShiSentenceLength = sentenceList.length;
            console.log(this.DingShiSentenceLength);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      //8.监听型句型长度
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            let sentenceList = initialSentenceList.filter((item) => {
              return item.name == "8";
            });
            this.MonitorSentenceLength = sentenceList.length;
            console.log(this.MonitorSentenceLength);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //////////////////////////////////
    //1.按照功能划分，有8种句型【命令型-赋值型-授权型-消息型-条件型-循环型-定时型-监听型】，这是获取命令型句型
    getMinglingSentenceList() {
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            this.sentenceData = initialSentenceList.filter((item) => {
              return item.name == "1";
            });
            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //2.按照功能划分，有8种句型【命令型-赋值型-授权型-消息型-条件型-循环型-定时型-监听型】，这是获取赋值型句型
    getFuZhiSentenceList() {
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            console.log(initialSentenceList);
            this.sentenceData = initialSentenceList.filter((item) => {
              return item.name == "2";
            });
            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },

    //3.按照功能划分，有8种句型【命令型-赋值性-授权型-消息型-条件型-循环型-定时型-监听型】，这是获取授权型句型
    getAuthenSentenceList() {
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            this.sentenceData = initialSentenceList.filter((item) => {
              return item.name == "4";
            });
            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //4.按照功能划分，有8种句型【命令型-赋值性-授权型-消息型-条件型-循环型-定时型-监听型】，这是获取消息型句型
    getMessageSentenceList() {
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            this.sentenceData = initialSentenceList.filter((item) => {
              return item.name == "3";
            }); //消息型改为数字3
            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //5.按照功能划分，有8种句型【命令型-赋值性-授权型-消息型-条件型-循环型-定时型-监听型】，这是获取条件型句型
    getConditionSentenceList() {
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            this.sentenceData = initialSentenceList.filter((item) => {
              return item.name == "7";
            });
            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //6.按照功能划分，有8种句型【命令型-赋值性-授权型-消息型-条件型-循环型-定时型-监听型】，这是获取循环型句型
    getLoopSentenceList() {
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            this.sentenceData = initialSentenceList.filter((item) => {
              return item.name == "9";
            });

            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //7.按照功能划分，有8种句型【命令型-赋值性-授权型-消息型-条件型-循环型-定时型-监听型】，这是获取定时型句型
    getDingShiSentenceList() {
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            this.sentenceData = initialSentenceList.filter((item) => {
              return item.name == "5";
            });
            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //8.按照功能划分，有8种句型【命令型-赋值性-授权型-消息型-条件型-循环型-定时型-监听型】，这是获取监听型句型
    getMonitorSentenceList() {
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            this.sentenceData = initialSentenceList.filter((item) => {
              return item.name == "8";
            });
            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //9.按照功能划分，有8种句型【命令型-赋值性-授权型-消息型-条件型-循环型-定时型-监听型-延时型】，这是获取监听型句型
    getDelaySentenceList() {
      sentenceService.getAllSentenceByStatus(
        { status: 3 },
        {
          onSuccess: (model, fModel, json) => {
            let initialSentenceList = json.data.rows.sentence.sort((a, b) => {
              //根据词汇创建的先后顺序进行展示
              const date1 = new Date(a.createTime).getTime();
              const date2 = new Date(b.createTime).getTime();
              return date2 - date1;
            });
            this.sentenceData = initialSentenceList.filter((item) => {
              return item.name == "6";
            });
            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    //就是tabs标签页切换时触发的方法
    handleSentenceNameChange(activeKey) {
      console.log(activeKey);
      if (activeKey == 1) {
        this.getMinglingSentenceList();
      } else if (activeKey == 2) {
        this.getFuZhiSentenceList();
      } else if (activeKey == 3) {
        //消息型还改为计算型

        this.getMessageSentenceList();
      } else if (activeKey == 4) {
        this.getAuthenSentenceList();
      } else if (activeKey == 5) {
        this.getDingShiSentenceList();
      } else if (activeKey == 6) {
        this.getDelaySentenceList();
      } else if (activeKey == 7) {
        this.getConditionSentenceList();
      } else if (activeKey == 8) {
        this.getMonitorSentenceList();
      } else if (activeKey == 9) {
        this.getLoopSentenceList();
      }
    },
    ////////////////////
    /*句型界面搜索功能，不管是句型这一列，还是句型实例这一列，还是编辑者这一列包含用户输入的关键字，都要检索到 */
    clearInput() {
      //清空搜索框里的内容
      this.userInputKeyword = "";
    },
    handleInput() {
      // 防抖
      let timer = setTimeout(() => {
        this.onSentenceSearch(this.userInputKeyword);
        clearTimeout(timer);
      }, 500);
    },
    onSentenceSearch(userInputKeyword) {
      console.log("用户输入的关键字为:", userInputKeyword);
      console.log("当前所处页面键为:", this.activeKey);
      /*根据用户输入的关键字搜索句型， */
      if (userInputKeyword.trim() == "") {
        this.handleSentenceNameChange(this.activeKey);
        return;
      }

      sentenceService.getAllSentencesByuserInputKeyword(
        { userInputKeyword },
        {
          onSuccess: (model, fModel, json) => {
            console.log("切换tab,进入界面");
            this.sentenceData = json.data.rows.sentence.filter((item) => {
              return item.name == this.activeKey;
            });
            console.log(this.sentenceData);
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
    toHome() {
      this.$router.push({ name: "Home" });
    },
    demandList(row, index) {
      return {
        on: {
          click: () => {
            console.log(row, "当前行数据");
            console.log(index, "当前行索引");
            this.unitText = row.unitText;
            this.description = row.description;
            this.sentenceDescription = row.sentenceDescription;

            this.sentenceInfoVisible = true;
          },
        },
      };
    },
    handleCancel(e) {
      console.log("句型句固件弹窗信息,点击了右上角的X,取消事件");
      this.sentenceInfoVisible = false;
    },

    /*当读者观看固件原代码这一栏时，由于代码长度太长，
    超出规定的限制后就展示“更多”字样，点击更多后会出现弹出一个弹窗，
    将如此多的代码显示出来*/
    showUnitCode(record) {
      this.unitCodeVisible = true;
      this.unitText = record.unitText;
    },
    handleUnitCodeCancel() {
      this.unitCodeVisible = false;
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
      const result = await sentenceService.getAllSentenceByStatus({
        status: 3,
      });
      this.data = result.rows;

      this.pagination.total = result.total;
      this.tableLoading = false;
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
  min-width: 1180px;
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
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;

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
    background: url("../assets/sentenceLib.png") no-repeat;
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