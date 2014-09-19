import kivy

kivy.require('1.8.0') # replace with your current kivy version !

from kivy.app import App
from kivy.uix.button import Button
from kivy.uix.boxlayout import BoxLayout
from kivy.core.audio import SoundLoader
from kivy.properties import StringProperty, ObjectProperty
from glob import glob
from os.path import dirname, join, basename

gRoot = None

class RobotGuideApp(App):
	
	sound = ObjectProperty(None, allownone=True)

	def  sound_stop(self, obj):
		global gRoot
		gRoot.ids.MainButton.text = 'Start Excusion'

	def excursion(self):
	    global gRoot
	    
	    gRoot = self.root

	    if self.sound is None:
	        self.sound = SoundLoader.load('sounds/welcome.mp3')
	        self.sound.bind( on_stop=self.sound_stop )
	    
	    # stop the sound if it's currently playing
	    if self.sound.status != 'stop':
	    	self.sound.stop()
	    	self.root.ids.MainButton.text = 'Start Excusion'
	    else:
	    	self.sound.play()
	    	self.root.ids.MainButton.text = 'Stop Excusion'

if __name__ == '__main__':
    RobotGuideApp().run()
