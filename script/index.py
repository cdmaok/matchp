#coding=utf-8

# this file is to add index by certain file.

import urllib2,os,urllib,sys,time

url = 'http://localhost:8080'
api = '/matchp-web/api/index'

def addIndex(filepath):
	weibos = [ line.strip() for line in open(filepath).readlines()]
	for index,weibo in enumerate(weibos):
		fetch_weibo(url+api,weibo)
		if index % 1000 == 0:
			time.sleep(60)

def fetch_weibo(url,text):
	#data = urllib.urlencode(text)
	req = urllib2.Request(url,text)
	req.add_header('Content-Type', 'application/json')
	try:
		res = urllib2.urlopen(req)
		data = res.read()
		print data
	except urllib2.HTTPError as e:
		print text
		print e.code
		print e.read()


if __name__ == '__main__':
	if len(sys.argv) != 2:
		print 'need a file.'
		exit;
	else:
		filepath = sys.argv[1]
		if not os.path.exists(filepath):
			print filepath, 'no exists.'
			exit
		addIndex(filepath)
		print 'finish indexing.'

