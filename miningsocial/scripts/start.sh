source /home/vagrant/.virtualenvs/miningsocial/bin/activate
pip install -q -q -q -r /vagrant/requirements.txt
cd /vagrant/notebooks
jupyter notebook --allow-root --ip=0.0.0.0 --no-browser &
sleep 2
echo 'Connect to Jupter Notebook by inputing the URL below into your browser.'
echo 'If your host machine is windows please replace the ip with 127.0.0.1'
jupyter notebook list
