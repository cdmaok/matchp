#coding=utf-8

# this file is to add index by certain file.

import urllib2,os,urllib,sys,time

url = 'http://localhost:8080'
api = '/matchp-web/api/index'

def addIndex(filepath,indexType,start=0):
	weibos = [ line.strip() for line in open(filepath).readlines()]
	size = len(weibos[start:])
	for index,weibo in enumerate(weibos[start:]):
		fetch_weibo(url+api + '?type=' + indexType,weibo)
		print 'finish %d / %d' %(index + start ,size + start)
		if index % 1000 == 0:
			time.sleep(60)

def fetch_weibo(url,text):
	#data = urllib.urlencode(text)
	req = urllib2.Request(url,text)
	req.add_header('Content-Type', 'application/json')
	try:
		res = urllib2.urlopen(req)
		data = res.read()
	except urllib2.HTTPError as e:
		print text
		print e.code
		print e.read()


if __name__ == '__main__':
	if len(sys.argv) <= 3:
		print 'need a file. and type'
		sys.exit()
	else:
		filepath = sys.argv[1]
		indexType = sys.argv[2]
		start = 0
		if len(sys.argv) == 4: start = int(sys.argv[3])
		if not os.path.exists(filepath):
			print filepath, 'no exists.'
			exit
		addIndex(filepath,indexType,start)
		print 'finish indexing.'

