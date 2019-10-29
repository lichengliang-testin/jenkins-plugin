package cn.testin.plugins.testinpro.bean;

import cn.testin.plugins.testinpro.annotation.Nullable;
import cn.testin.plugins.testinpro.enums.ResultCategoryEnum;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 * date 2019/10/25
 * <p>
 * 任务总结
 */
public class TaskSummary implements Serializable {
    private static final long serialVersionUID = -14L;

    /**
     * 子任务维度
     */
    private SummaryInfo subtaskResult;
    /**
     * 子子任务维度
     */
    private SummaryInfo testResult;

    private static final String TASK_SUMMARY = "taskSummary";
    private static final String SUB_TASK_RESULT = "subtaskResult";
    private static final String TEST_RESULT = "testResult";
    private static final String VAL = "val";
    private static final String RESULT_CATEGORY = "resultCategory";

    public Integer getTaskResultCategory() {
        Integer resultCategory = getSubTaskResultCategory();
        if (isEmpty(resultCategory)) {
            return getSubSubTaskResultCategory();
        }
        return resultCategory;
    }

    public SummaryInfo getTestResult() {
        return testResult;
    }

    @Nullable
    public static TaskSummary parse(String content) {
        if (isEmpty(content)) {
            return null;
        }
        JSONObject jsonContent = JSONObject.fromObject(content);

        if (!jsonContent.containsKey(TASK_SUMMARY)) {
            return null;
        }

        JSONObject taskSummary = jsonContent.getJSONObject(TASK_SUMMARY);

        TaskSummary summary = new TaskSummary();
        if (taskSummary.containsKey(SUB_TASK_RESULT)) {
            summary.subtaskResult = parseSummary(taskSummary.getJSONArray(SUB_TASK_RESULT));
        }

        if (taskSummary.containsKey(SUB_TASK_RESULT)) {
            summary.testResult = parseSummary(taskSummary.getJSONArray(TEST_RESULT));
        }

        return summary;
    }

    @Nullable
    private static SummaryInfo parseSummary(JSONArray array) {
        if (isEmpty(array)) {
            return null;
        }

        SummaryInfo summaryInfo = new SummaryInfo();
        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            Node node = parseNode(array.getJSONObject(i));
            if (isEmpty(node)) {
                continue;
            }
            nodes.add(node);
        }
        summaryInfo.nodes = nodes;
        return summaryInfo;
    }

    @Nullable
    private static Node parseNode(JSONObject jsonObject) {
        if (isEmpty(jsonObject)) {
            return null;
        }

        Node node = new Node();
        if (jsonObject.containsKey(RESULT_CATEGORY)) {
            node.resultCategory = jsonObject.getInt(RESULT_CATEGORY);
        }
        if (jsonObject.containsKey(VAL)) {
            node.val = jsonObject.getInt(VAL);
        }
        return node;
    }

    @Nullable
    private Integer getSubTaskResultCategory() {
        if (isEmpty(subtaskResult) || isEmpty(subtaskResult.nodes)) {
            return null;
        }

        return compareResultCategory(subtaskResult.nodes);
    }

    @Nullable
    private Integer getSubSubTaskResultCategory() {
        if (isEmpty(testResult) || isEmpty(testResult.nodes)) {
            return null;
        }

        return compareResultCategory(testResult.nodes);
    }

    private Integer compareResultCategory(List<Node> nodes) {
        if (isEmpty(nodes)) {
            return null;
        }
        Integer resultCategory = null;
        for (Node node : nodes) {
            if (isEmpty(node.resultCategory)) {
                continue;
            }
            int indexOf = ResultCategoryEnum.implementErrorcodes().indexOf(node.resultCategory);
            if (isEmpty(resultCategory)) {
                resultCategory = node.resultCategory;
                continue;
            }
            if (indexOf < ResultCategoryEnum.implementErrorcodes().indexOf(resultCategory)) {
                resultCategory = node.resultCategory;
            }
        }
        return resultCategory;
    }

    public static class SummaryInfo {
        private List<Node> nodes;

        public List<Node> getNodes() {
            return nodes;
        }
    }

    public static class Node {
        private Integer resultCategory;
        private Integer val;

        public Integer getResultCategory() {
            return resultCategory;
        }

        public Integer getVal() {
            return val;
        }
    }
}
