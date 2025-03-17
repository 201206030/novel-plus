INSERT INTO crawl_source (source_name, crawl_rule, source_status, create_time, update_time)
VALUES ('香书小说网', '{
  "bookListUrl": "http://www.xbiqugu.la/fenlei/{catId}_{page}.html",
  "catIdRule": {
    "catId1": "1",
    "catId2": "2",
    "catId3": "3",
    "catId4": "4",
    "catId5": "6",
    "catId6": "5"
  },
  "bookIdPatten": "<a\\\\s+href=\\"http://www.xbiqugu.la/(\\\\d+/\\\\d+)/\\"\\\\s+target=\\"_blank\\">",
  "pagePatten": "<em\\\\s+id=\\"pagestats\\">(\\\\d+)/\\\\d+</em>",
  "totalPagePatten": "<em\\\\s+id=\\"pagestats\\">\\\\d+/(\\\\d+)</em>",
  "bookDetailUrl": "http://www.xbiqugu.la/{bookId}/",
  "bookNamePatten": "<h1>([^/]+)</h1>",
  "authorNamePatten": "者：([^/]+)</p>",
  "picUrlPatten": "src=\\"(http://www.xbiqugu.la/files/article/image/\\\\d+/\\\\d+/\\\\d+s\\\\.jpg)\\"",
  "bookStatusRule": {},
  "descStart": "<div id=\\"intro\\">",
  "descEnd": "</div>",
  "upadateTimePatten": "<p>最后更新：(\\\\d+-\\\\d+-\\\\d+\\\\s\\\\d+:\\\\d+:\\\\d+)</p>",
  "upadateTimeFormatPatten": "yyyy-MM-dd HH:mm:ss",
  "bookIndexUrl": "http://www.xbiqugu.la/{bookId}/",
  "indexIdPatten": "<a\\\\s+href=''/\\\\d+/\\\\d+/(\\\\d+)\\\\.html''\\\\s+>[^/]+</a>",
  "indexNamePatten": "<a\\\\s+href=''/\\\\d+/\\\\d+/\\\\d+\\\\.html''\\\\s+>([^/]+)</a>",
  "bookContentUrl": "http://www.xbiqugu.la/{bookId}/{indexId}.html",
  "contentStart": "<div id=\\"content\\">",
  "contentEnd": "<p>",
  "filterContent":"<div\\\\s+id=\\"content_tip\\">\\\\s*<b>([^/]+)</b>\\\\s*</div>"
}', 0, '2024-06-01 10:11:39', '2024-06-01 10:11:39');


update crawl_source
set crawl_rule = replace(crawl_rule, 'ibiquzw.org', 'biquxs.info')
where id = 16;

delete
from sys_menu
where menu_id = 104;

delete
from sys_menu
where menu_id = 57;