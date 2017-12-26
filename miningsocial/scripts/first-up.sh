sudo add-apt-repository -y ppa:jonathonf/python-3.6
sudo apt-get update
sudo apt install -y python3.6
sudo apt install -y python3.6-dev
wget https://bootstrap.pypa.io/get-pip.py
sudo python3.6 get-pip.py

# Virtual Environments
sudo pip3.6 install virtualenv
mkdir /home/vagrant/.virtualenvs
virtualenv /home/vagrant/.virtualenvs/miningsocial
source /home/vagrant/.virtualenvs/miningsocial/bin/activate
pip3.6 install -r /vagrant/requirements.txt

# Jupyter Password
mkdir /home/vagrant/.jupyter
cp /vagrant/jupyter_notebook_config.json /home/vagrant/.jupyter/jupyter_notebook_config.json

# Cleanup
rm ~/get-pip.py -f
