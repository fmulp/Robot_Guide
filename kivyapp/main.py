import kivy

kivy.require('1.8.0') # replace with your current kivy version !

from kivy.app import App
from kivy.clock import Clock
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.image import Image
from kivy.uix.boxlayout import BoxLayout
from kivy.core.audio import SoundLoader
from kivy.properties import StringProperty, ObjectProperty
from kivy.network.urlrequest import UrlRequest

from glob import glob
from os.path import dirname, join, basename
from functools import partial
from time import sleep

import parsexml

XMLFILEPATH = 'excursion.xml'
HOST = 'http://172.26.201.128:808/cgi-bin/forward'
#HOST = 'http://172.26.201.7:8080/cgi-bin/forward.exe'
SPEED = 90


class RobotGuideApp(App):
	
	sound = ObjectProperty(None, allownone=True)

	@property
	def xml(self):
		if not hasattr(self, '_xml'):
			self._xml = parsexml.load_xml(XMLFILEPATH)
		return self._xml
		
	@xml.setter
	def xml(self, path):
		self._xml = parsexml.load_xml(path) 

	@property
	def exhibits(self, index=0, name=None):
		self._exhibits = parsexml.get_exhibits(parsexml.get_excursion(self.xml, index=index, name=name))
		return self._exhibits

	@exhibits.setter
	def exhibits(self, value):
		self._exhibits = value
	@property

	def currentindex(self):
		if not hasattr(self, '_currentindex'):
			self._currentindex = 0
		return self._currentindex

	@currentindex.setter
	def currentindex(self, value):
		self._currentindex = value

	def show(self, *widgets):
		self.root.clear_widgets()
		if not widgets:
			widgets = self.root.ids
		for widget in widgets:
			self.root.add_widget(widget)


	def scheduler(self, dt=0):
		if self.currentindex < len(self.exhibits) - 2:
			self.currentindex += 1
			Clock.schedule_once(self.move_to_exhibit, 0)
		else:
			self.show()
			self.root.ids.MainButton.text = 'Start over'

	def move_to_exhibit(self, dt=0, index=None):
		if index is None:
			index = self.currentindex

		data = self.exhibits[index]
		label = Label(text=data['description'], font_size=self.root.ids.MainButton.font_size)
		label.size_hint_y = 0.3
		image = Image(source=data['visualurl'], scale=2)
		image.allow_stretch = True
		self.show(label, image)

		reqstring = HOST + '?' + str(data['x']) + '&' + str(data['y']) + '&' + str(data['phi'])
		req = UrlRequest(reqstring)

		sleep(int((int(data['x']) ** 2 + int(data['y']) ** 2) ** 0.5 / float(SPEED)))
		self.sound = SoundLoader.load(data['audiourl'])
		self.sound.play() 
		Clock.schedule_once(self.scheduler, self.sound.length + 7)


if __name__ == '__main__':
	RobotGuideApp().run()
