from xml.dom import minidom

def load_xml(path):
	return minidom.parse(path)

def get_excursion(xmldoc, index=0, name=None):
	if name:
		index = [elem.getAttribute('name') for elem in xmldoc.getElementsByTagName('ExcursionTitle')].index(name)
	return xmldoc.getElementsByTagName('ExcursionTitle')[index]

def get_exhibits(xmlexcursion):
	result = {}
	for exhibit in xmlexcursion.getElementsByTagName('exhibit'):
		result[exhibit.getAttribute('id')] = {
			'x' : float(exhibit.getElementsByTagName('x')[0].firstChild.nodeValue),
			'y' : float(exhibit.getElementsByTagName('y')[0].firstChild.nodeValue),
			'phi' : float(exhibit.getElementsByTagName('phi')[0].firstChild.nodeValue),
			'description' : exhibit.getElementsByTagName('description')[0].firstChild.nodeValue,
			'audiourl' : exhibit.getElementsByTagName('audio')[0].getAttribute('URL'),
			'visualrl' : exhibit.getElementsByTagName('visual')[0].getAttribute('URL')
		}
	return result


if __name__ == '__main__':
	xmldoc = load_xml(raw_input('Pease input xml file path:'))
	xmlexcursion = get_excursion(xmldoc)
	raw_input(get_exhibits(xmlexcursion))
		