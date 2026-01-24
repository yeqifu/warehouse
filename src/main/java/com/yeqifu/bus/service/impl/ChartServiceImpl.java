package com.yeqifu.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeqifu.bus.entity.*;
import com.yeqifu.bus.mapper.*;
import com.yeqifu.bus.service.IChartService;
import com.yeqifu.bus.vo.ChartDataVo;
import com.yeqifu.bus.vo.ChartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 图表统计服务实现类
 * @author system
 * @since 2024-01-24
 */
@Service
public class ChartServiceImpl implements IChartService {

    @Autowired
    private SalesMapper salesMapper;

    @Autowired
    private SalesbackMapper salesbackMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private InportMapper inportMapper;

    @Autowired
    private OutportMapper outportMapper;

    @Override
    public Map<String, Object> getSalesStatistics(ChartVo chartVo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 月度销售趋势
            List<ChartDataVo> monthlyTrend = getMonthlySalesTrend(chartVo);
            result.put("monthlyTrend", monthlyTrend);
            
            // 商品销售排行
            List<ChartDataVo> topGoods = getTopSalesGoods(chartVo);
            result.put("topGoods", topGoods);
            
            // 客户购买排行
            List<ChartDataVo> topCustomers = getTopCustomers(chartVo);
            result.put("topCustomers", topCustomers);
            
            result.put("code", 200);
            result.put("msg", "获取销售统计数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
            result.put("msg", "获取销售统计数据失败");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getSalesbackStatistics(ChartVo chartVo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 月度退货趋势
            List<ChartDataVo> monthlyTrend = getMonthlySalesbackTrend(chartVo);
            result.put("monthlyTrend", monthlyTrend);
            
            // 退货商品排行
            List<ChartDataVo> topReturnGoods = getTopReturnGoods(chartVo);
            result.put("topReturnGoods", topReturnGoods);
            
            // 退货率统计
            List<ChartDataVo> returnRate = getReturnRateByGoods(chartVo);
            result.put("returnRate", returnRate);
            
            result.put("code", 200);
            result.put("msg", "获取退货统计数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
            result.put("msg", "获取退货统计数据失败");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getGoodsStockStatistics(ChartVo chartVo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 库存预警商品
            List<ChartDataVo> lowStockGoods = getLowStockGoods();
            result.put("lowStockGoods", lowStockGoods);
            
            // 商品分类库存统计
            List<ChartDataVo> categoryStock = getCategoryStock();
            result.put("categoryStock", categoryStock);
            
            // 库存总值统计
            Double totalStockValue = getTotalStockValue();
            result.put("totalStockValue", totalStockValue);
            
            result.put("code", 200);
            result.put("msg", "获取库存统计数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
            result.put("msg", "获取库存统计数据失败");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getCustomerStatistics(ChartVo chartVo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 客户购买排行
            List<ChartDataVo> topCustomers = getTopCustomers(chartVo);
            result.put("topCustomers", topCustomers);
            
            // 客户退货排行
            List<ChartDataVo> topReturnCustomers = getTopReturnCustomers(chartVo);
            result.put("topReturnCustomers", topReturnCustomers);
            
            // 客户活跃度统计
            List<ChartDataVo> customerActivity = getCustomerActivity();
            result.put("customerActivity", customerActivity);
            
            result.put("code", 200);
            result.put("msg", "获取客户统计数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
            result.put("msg", "获取客户统计数据失败");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getDashboardData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 今日销售总额
            Double todaySalesAmount = getTodaySalesAmount();
            result.put("todaySalesAmount", todaySalesAmount);
            
            // 本月销售总额
            Double monthSalesAmount = getMonthSalesAmount();
            result.put("monthSalesAmount", monthSalesAmount);
            
            // 库存预警数量
            Integer lowStockCount = getLowStockCount();
            result.put("lowStockCount", lowStockCount);
            
            // 本月退货率
            Double monthReturnRate = getMonthReturnRate();
            result.put("monthReturnRate", monthReturnRate);
            
            result.put("code", 200);
            result.put("msg", "获取仪表板数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
            result.put("msg", "获取仪表板数据失败");
        }
        
        return result;
    }

    /**
     * 获取月度销售趋势
     */
    private List<ChartDataVo> getMonthlySalesTrend(ChartVo chartVo) {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            QueryWrapper<Sales> queryWrapper = new QueryWrapper<>();
            if (chartVo.getStartTime() != null) {
                queryWrapper.ge("salestime", chartVo.getStartTime());
            }
            if (chartVo.getEndTime() != null) {
                queryWrapper.le("salestime", chartVo.getEndTime());
            }
            
            List<Sales> salesList = salesMapper.selectList(queryWrapper);
            
            // 按月份分组统计
            Map<String, Double> monthlyData = new LinkedHashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            
            for (Sales sales : salesList) {
                String month = sdf.format(sales.getSalestime());
                Double amount = sales.getSaleprice() * sales.getNumber();
                monthlyData.put(month, monthlyData.getOrDefault(month, 0.0) + amount);
            }
            
            for (Map.Entry<String, Double> entry : monthlyData.entrySet()) {
                result.add(new ChartDataVo(entry.getKey(), entry.getValue()));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取销售排行商品
     */
    private List<ChartDataVo> getTopSalesGoods(ChartVo chartVo) {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            QueryWrapper<Sales> queryWrapper = new QueryWrapper<>();
            if (chartVo.getStartTime() != null) {
                queryWrapper.ge("salestime", chartVo.getStartTime());
            }
            if (chartVo.getEndTime() != null) {
                queryWrapper.le("salestime", chartVo.getEndTime());
            }
            
            List<Sales> salesList = salesMapper.selectList(queryWrapper);
            
            // 按商品分组统计
            Map<Integer, Double> goodsSalesData = new HashMap<>();
            for (Sales sales : salesList) {
                Double amount = sales.getSaleprice() * sales.getNumber();
                goodsSalesData.put(sales.getGoodsid(), goodsSalesData.getOrDefault(sales.getGoodsid(), 0.0) + amount);
            }
            
            // 获取商品名称并排序
            List<Map.Entry<Integer, Double>> sortedList = new ArrayList<>(goodsSalesData.entrySet());
            sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            // 取前10名
            int count = 0;
            for (Map.Entry<Integer, Double> entry : sortedList) {
                if (count >= 10) break;
                
                Goods goods = goodsMapper.selectById(entry.getKey());
                if (goods != null) {
                    result.add(new ChartDataVo(goods.getGoodsname(), entry.getValue()));
                    count++;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取购买排行客户
     */
    private List<ChartDataVo> getTopCustomers(ChartVo chartVo) {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            QueryWrapper<Sales> queryWrapper = new QueryWrapper<>();
            if (chartVo.getStartTime() != null) {
                queryWrapper.ge("salestime", chartVo.getStartTime());
            }
            if (chartVo.getEndTime() != null) {
                queryWrapper.le("salestime", chartVo.getEndTime());
            }
            
            List<Sales> salesList = salesMapper.selectList(queryWrapper);
            
            // 按客户分组统计
            Map<Integer, Double> customerSalesData = new HashMap<>();
            for (Sales sales : salesList) {
                Double amount = sales.getSaleprice() * sales.getNumber();
                customerSalesData.put(sales.getCustomerid(), customerSalesData.getOrDefault(sales.getCustomerid(), 0.0) + amount);
            }
            
            // 获取客户名称并排序
            List<Map.Entry<Integer, Double>> sortedList = new ArrayList<>(customerSalesData.entrySet());
            sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            // 取前10名
            int count = 0;
            for (Map.Entry<Integer, Double> entry : sortedList) {
                if (count >= 10) break;
                
                Customer customer = customerMapper.selectById(entry.getKey());
                if (customer != null) {
                    result.add(new ChartDataVo(customer.getCustomername(), entry.getValue()));
                    count++;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取月度退货趋势
     */
    private List<ChartDataVo> getMonthlySalesbackTrend(ChartVo chartVo) {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            QueryWrapper<Salesback> queryWrapper = new QueryWrapper<>();
            if (chartVo.getStartTime() != null) {
                queryWrapper.ge("salesbacktime", chartVo.getStartTime());
            }
            if (chartVo.getEndTime() != null) {
                queryWrapper.le("salesbacktime", chartVo.getEndTime());
            }
            
            List<Salesback> salesbackList = salesbackMapper.selectList(queryWrapper);
            
            // 按月份分组统计
            Map<String, Double> monthlyData = new LinkedHashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            
            for (Salesback salesback : salesbackList) {
                String month = sdf.format(salesback.getSalesbacktime());
                Double amount = salesback.getSalebackprice() * salesback.getNumber();
                monthlyData.put(month, monthlyData.getOrDefault(month, 0.0) + amount);
            }
            
            for (Map.Entry<String, Double> entry : monthlyData.entrySet()) {
                result.add(new ChartDataVo(entry.getKey(), entry.getValue()));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取退货排行商品
     */
    private List<ChartDataVo> getTopReturnGoods(ChartVo chartVo) {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            QueryWrapper<Salesback> queryWrapper = new QueryWrapper<>();
            if (chartVo.getStartTime() != null) {
                queryWrapper.ge("salesbacktime", chartVo.getStartTime());
            }
            if (chartVo.getEndTime() != null) {
                queryWrapper.le("salesbacktime", chartVo.getEndTime());
            }
            
            List<Salesback> salesbackList = salesbackMapper.selectList(queryWrapper);
            
            // 按商品分组统计
            Map<Integer, Double> goodsReturnData = new HashMap<>();
            for (Salesback salesback : salesbackList) {
                Double amount = salesback.getSalebackprice() * salesback.getNumber();
                goodsReturnData.put(salesback.getGoodsid(), goodsReturnData.getOrDefault(salesback.getGoodsid(), 0.0) + amount);
            }
            
            // 获取商品名称并排序
            List<Map.Entry<Integer, Double>> sortedList = new ArrayList<>(goodsReturnData.entrySet());
            sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            // 取前10名
            int count = 0;
            for (Map.Entry<Integer, Double> entry : sortedList) {
                if (count >= 10) break;
                
                Goods goods = goodsMapper.selectById(entry.getKey());
                if (goods != null) {
                    result.add(new ChartDataVo(goods.getGoodsname(), entry.getValue()));
                    count++;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取商品退货率
     */
    private List<ChartDataVo> getReturnRateByGoods(ChartVo chartVo) {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            // 获取销售数据
            QueryWrapper<Sales> salesQueryWrapper = new QueryWrapper<>();
            if (chartVo.getStartTime() != null) {
                salesQueryWrapper.ge("salestime", chartVo.getStartTime());
            }
            if (chartVo.getEndTime() != null) {
                salesQueryWrapper.le("salestime", chartVo.getEndTime());
            }
            List<Sales> salesList = salesMapper.selectList(salesQueryWrapper);
            
            // 获取退货数据
            QueryWrapper<Salesback> salesbackQueryWrapper = new QueryWrapper<>();
            if (chartVo.getStartTime() != null) {
                salesbackQueryWrapper.ge("salesbacktime", chartVo.getStartTime());
            }
            if (chartVo.getEndTime() != null) {
                salesbackQueryWrapper.le("salesbacktime", chartVo.getEndTime());
            }
            List<Salesback> salesbackList = salesbackMapper.selectList(salesbackQueryWrapper);
            
            // 计算退货率
            Map<Integer, Integer> salesCountMap = new HashMap<>();
            Map<Integer, Integer> returnCountMap = new HashMap<>();
            
            for (Sales sales : salesList) {
                salesCountMap.put(sales.getGoodsid(), salesCountMap.getOrDefault(sales.getGoodsid(), 0) + sales.getNumber());
            }
            
            for (Salesback salesback : salesbackList) {
                returnCountMap.put(salesback.getGoodsid(), returnCountMap.getOrDefault(salesback.getGoodsid(), 0) + salesback.getNumber());
            }
            
            // 计算退货率并排序
            List<Map.Entry<Integer, Double>> returnRateList = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : salesCountMap.entrySet()) {
                Integer goodsId = entry.getKey();
                Integer salesCount = entry.getValue();
                Integer returnCount = returnCountMap.getOrDefault(goodsId, 0);
                
                if (salesCount > 0) {
                    Double returnRate = (double) returnCount / salesCount * 100;
                    returnRateList.add(new AbstractMap.SimpleEntry<>(goodsId, returnRate));
                }
            }
            
            returnRateList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            // 取前10名
            int count = 0;
            for (Map.Entry<Integer, Double> entry : returnRateList) {
                if (count >= 10) break;
                
                Goods goods = goodsMapper.selectById(entry.getKey());
                if (goods != null) {
                    result.add(new ChartDataVo(goods.getGoodsname(), entry.getValue()));
                    count++;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取退货排行客户
     */
    private List<ChartDataVo> getTopReturnCustomers(ChartVo chartVo) {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            QueryWrapper<Salesback> queryWrapper = new QueryWrapper<>();
            if (chartVo.getStartTime() != null) {
                queryWrapper.ge("salesbacktime", chartVo.getStartTime());
            }
            if (chartVo.getEndTime() != null) {
                queryWrapper.le("salesbacktime", chartVo.getEndTime());
            }
            
            List<Salesback> salesbackList = salesbackMapper.selectList(queryWrapper);
            
            // 按客户分组统计
            Map<Integer, Double> customerReturnData = new HashMap<>();
            for (Salesback salesback : salesbackList) {
                Double amount = salesback.getSalebackprice() * salesback.getNumber();
                customerReturnData.put(salesback.getCustomerid(), customerReturnData.getOrDefault(salesback.getCustomerid(), 0.0) + amount);
            }
            
            // 获取客户名称并排序
            List<Map.Entry<Integer, Double>> sortedList = new ArrayList<>(customerReturnData.entrySet());
            sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            // 取前10名
            int count = 0;
            for (Map.Entry<Integer, Double> entry : sortedList) {
                if (count >= 10) break;
                
                Customer customer = customerMapper.selectById(entry.getKey());
                if (customer != null) {
                    result.add(new ChartDataVo(customer.getCustomername(), entry.getValue()));
                    count++;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取库存预警商品
     */
    private List<ChartDataVo> getLowStockGoods() {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lt("number", 10); // 库存少于10的商品
            List<Goods> goodsList = goodsMapper.selectList(queryWrapper);
            
            for (Goods goods : goodsList) {
                result.add(new ChartDataVo(goods.getGoodsname(), (double) goods.getNumber()));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取商品分类库存统计
     */
    private List<ChartDataVo> getCategoryStock() {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            List<Goods> goodsList = goodsMapper.selectList(null);
            
            // 按商品产地分组统计（因为没有商品类型字段，使用产地作为分类）
            Map<String, Integer> categoryStockMap = new HashMap<>();
            for (Goods goods : goodsList) {
                String category = goods.getProduceplace() != null ? goods.getProduceplace() : "其他";
                categoryStockMap.put(category, categoryStockMap.getOrDefault(category, 0) + goods.getNumber());
            }
            
            for (Map.Entry<String, Integer> entry : categoryStockMap.entrySet()) {
                result.add(new ChartDataVo(entry.getKey(), (double) entry.getValue()));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取库存总值
     */
    private Double getTotalStockValue() {
        Double totalValue = 0.0;
        
        try {
            List<Goods> goodsList = goodsMapper.selectList(null);
            
            for (Goods goods : goodsList) {
                // 使用销售价作为库存价值计算，因为没有进价字段
                totalValue += goods.getPrice() * goods.getNumber();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return totalValue;
    }

    /**
     * 获取客户活跃度
     */
    private List<ChartDataVo> getCustomerActivity() {
        List<ChartDataVo> result = new ArrayList<>();
        
        try {
            List<Customer> customerList = customerMapper.selectList(null);
            List<Sales> salesList = salesMapper.selectList(null);
            
            // 统计每个客户的购买次数
            Map<Integer, Integer> customerActivityMap = new HashMap<>();
            for (Sales sales : salesList) {
                customerActivityMap.put(sales.getCustomerid(), customerActivityMap.getOrDefault(sales.getCustomerid(), 0) + 1);
            }
            
            // 分类统计
            int highActivity = 0; // 高活跃度（购买次数>5）
            int mediumActivity = 0; // 中活跃度（购买次数2-5）
            int lowActivity = 0; // 低活跃度（购买次数1）
            int noActivity = 0; // 无活跃度（购买次数0）
            
            for (Customer customer : customerList) {
                Integer activity = customerActivityMap.getOrDefault(customer.getId(), 0);
                if (activity > 5) {
                    highActivity++;
                } else if (activity >= 2) {
                    mediumActivity++;
                } else if (activity == 1) {
                    lowActivity++;
                } else {
                    noActivity++;
                }
            }
            
            result.add(new ChartDataVo("高活跃度", (double) highActivity));
            result.add(new ChartDataVo("中活跃度", (double) mediumActivity));
            result.add(new ChartDataVo("低活跃度", (double) lowActivity));
            result.add(new ChartDataVo("无活跃度", (double) noActivity));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 获取今日销售总额
     */
    private Double getTodaySalesAmount() {
        Double amount = 0.0;
        
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date startTime = cal.getTime();
            
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            Date endTime = cal.getTime();
            
            QueryWrapper<Sales> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("salestime", startTime, endTime);
            List<Sales> salesList = salesMapper.selectList(queryWrapper);
            
            for (Sales sales : salesList) {
                amount += sales.getSaleprice() * sales.getNumber();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return amount;
    }

    /**
     * 获取本月销售总额
     */
    private Double getMonthSalesAmount() {
        Double amount = 0.0;
        
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date startTime = cal.getTime();
            
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            Date endTime = cal.getTime();
            
            QueryWrapper<Sales> queryWrapper = new QueryWrapper<>();
            queryWrapper.between("salestime", startTime, endTime);
            List<Sales> salesList = salesMapper.selectList(queryWrapper);
            
            for (Sales sales : salesList) {
                amount += sales.getSaleprice() * sales.getNumber();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return amount;
    }

    /**
     * 获取库存预警数量
     */
    private Integer getLowStockCount() {
        Integer count = 0;
        
        try {
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
            queryWrapper.lt("number", 10);
            count = goodsMapper.selectCount(queryWrapper);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return count;
    }

    /**
     * 获取本月退货率
     */
    private Double getMonthReturnRate() {
        Double returnRate = 0.0;
        
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date startTime = cal.getTime();
            
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            Date endTime = cal.getTime();
            
            // 获取本月销售数量
            QueryWrapper<Sales> salesQueryWrapper = new QueryWrapper<>();
            salesQueryWrapper.between("salestime", startTime, endTime);
            List<Sales> salesList = salesMapper.selectList(salesQueryWrapper);
            Integer totalSalesCount = salesList.stream().mapToInt(Sales::getNumber).sum();
            
            // 获取本月退货数量
            QueryWrapper<Salesback> salesbackQueryWrapper = new QueryWrapper<>();
            salesbackQueryWrapper.between("salesbacktime", startTime, endTime);
            List<Salesback> salesbackList = salesbackMapper.selectList(salesbackQueryWrapper);
            Integer totalReturnCount = salesbackList.stream().mapToInt(Salesback::getNumber).sum();
            
            if (totalSalesCount > 0) {
                returnRate = (double) totalReturnCount / totalSalesCount * 100;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return returnRate;
    }
}