#Socket Server ws://sea.linkflower.link:2100/wss

#行情发送报文 {"cid_":"1","cmd_":1,"dateUnit_":"24H","lang_":"","memoizedIsInitialized":1,"tokenFrom_":"USD","tokenTo_":"CNY","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}
#行情发送报文字段：cid  固定传1
#行情发送报文字段：cmd  固定传1
#行情发送报文字段：dateUnit  固定传   dateUnit： day: 24h week: 7D，1month: 1M, 6month: 6M, 1years 1Y, 3years: 3Y
#行情发送报文字段：lang  语言
#行情发送报文字段：tokenFrom  如ETH
#行情发送报文字段：tokenTo  如USD ，BTC
# 收到行情应数据 {"amount_":"0","cid_":"","cmd_":2,"data_":[{"memoizedIsInitialized":-1,"priceDate_":"2023-02-10 13:20:00","priceFrom_":"0.9999954064985518","priceTime_":1676035200000,"priceTo_":"0.14642941972124135","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}],"dateUnit_":"24H","isReal_":true,"memoizedIsInitialized":1,"priceFrom_":"0.9999954064985518","priceTo_":"0.14642941972124135","tokenFrom_":"USD","tokenTo_":"CNY","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}

#行情停止获取 发送报文{"cid_":"1","cmd_":3,"memoizedIsInitialized":1,"unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}
#行情停止发送报文字段：cid  固定传1 
#行情停止发送报文字段：cmd  固定传3

#获取一个币对价格比
#发送报文{"cid_":"3","cmd_":5,"lang_":"","memoizedIsInitialized":1,"tokens_":"USD,BTC,USDT,DOGE,ETH,FJD","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}
#发送报文字段：cid  固定传3
#发送报文字段：cmd  固定传5
#发送报文字段：lang 
#发送报文字段：tokens 如：USD,BTC,USDT,DOGE,ETH,FJD
#收到一个币对的价格比数据：
# {"cid_":"","cmd_":6,"data_":{"converter":{},"isMutable":false,"mapData":{"USDT":{"memoizedIsInitialized":-1,"price_":"1.0001436748934427","token_":"USDT","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}},"mode":"MAP"},"isReal_":true,"memoizedIsInitialized":1,"updateDate_":"2023-02-10 13:36:25","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}

#获取当前位置默认货币
#发送报文 {"cid_":"7","cmd_":11,"count_":6,"lang_":"","location_":"CN","memoizedIsInitialized":1,"unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}
#发送报文字段：cid  固定传7
#发送报文字段：cmd  固定传11
#发送报文字段：lang  
#发送报文字段：location  国家
# 收到当前位置默认货币数据: 
#{"cid_":"0","cmd_":12,"data_":[{"baseUnit_":0,"cid_":"","cmd_":0,"countryCode_":"CN","currencyType_":"currency","icon_":"https://sea.linkflower.link/icons/CNY@3x.png","memoizedIsInitialized":-1,"name_":"China Yuan Renminbi","price_":"0.14642941972124135","token_":"CNY","unitName_":"¥","updateDate_":"","usdAmount_":"","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0},{"baseUnit_":0,"cid_":"","cmd_":0,"countryCode_":"1","currencyType_":"digital","icon_":"https://sea.linkflower.link/icons/BTC@3x.png","memoizedIsInitialized":-1,"name_":"Bitcoin","price_":"21774.68248485321","token_":"BTC","unitName_":"₿","updateDate_":"","usdAmount_":"","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0},{"baseUnit_":0,"cid_":"","cmd_":0,"countryCode_":"1","currencyType_":"digital","icon_":"https://sea.linkflower.link/icons/USDT@3x.png","memoizedIsInitialized":-1,"name_":"Tether","price_":"1.0001404414644994","token_":"USDT","unitName_":"☼","updateDate_":"","usdAmount_":"","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0},{"baseUnit_":0,"cid_":"","cmd_":0,"countryCode_":"1","currencyType_":"digital","icon_":"https://sea.linkflower.link/icons/DOGE@3x.png","memoizedIsInitialized":-1,"name_":"Dogecoin","price_":"0.08174029878608363","token_":"DOGE","unitName_":"☼","updateDate_":"","usdAmount_":"","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0},{"baseUnit_":0,"cid_":"","cmd_":0,"countryCode_":"1","currencyType_":"digital","icon_":"https://sea.linkflower.link/icons/ETH@3x.png","memoizedIsInitialized":-1,"name_":"Ethereum","price_":"1537.824417085369","token_":"ETH","unitName_":"Ξ","updateDate_":"","usdAmount_":"","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0},{"baseUnit_":0,"cid_":"","cmd_":0,"countryCode_":"JP","currencyType_":"currency","icon_":"https://sea.linkflower.link/icons/JPY@3x.png","memoizedIsInitialized":-1,"name_":"Japan Yen","price_":"0.007660844418672615","token_":"JPY","unitName_":"¥","updateDate_":"","usdAmount_":"","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}],"memoizedIsInitialized":1,"updateDate_":"2023-02-10 13:39:54","unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}


#获取货币类型列表
#发送报文  {"cid_":"8","cmd_":9,"lang_":"","memoizedIsInitialized":1,"unknownFields":{"fields":{}},"memoizedSize":-1,"memoizedHashCode":0}
#发送报文字段：cid  固定传8
#发送报文字段：cmd  固定传9
#发送报文字段：lang  
#  收到货币类型列表数据: 