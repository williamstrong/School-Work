import unittest
from social_networks.database_controller.database import Database
from social_networks.database_controller.write import WriteToDatabase
from social_networks.database_controller.read import ReadFromDatabase


class TestDatabase(unittest.TestCase):
    def setUp(self):
        self.db = Database("test")

    # insure connection is open to the proper database
    def test_connection_open(self):
        db_name = str(self.db.client.name)
        self.assertTrue(db_name != None)

    def test_create_collection(self):
        self.db.create_collection("test")
        self.assertTrue(in_list(self.db.collections()))

    def test_remove_collection(self):
        self.db.remove_collection("test")
        self.assertFalse(in_list(self.db.collections()))

    def tearDown(self):
        self.db.remove_collection("test")

def in_list(collection_list):
    """Return true if test is in collection"""
    if "test" in str(collection_list): return True
    else: return False


class TestWrite(unittest.TestCase):
    def setUp(self):
        self.collection = "test"

        self.write_db = WriteToDatabase("test", self.collection)
        self.data = {'TestWrite': 'pass'}


    def test_add_data(self):
        num = self.write_db.add_data(self.data)
        self.assertTrue(num != None)

    def tearDown(self):
        self.write_db.remove_collection("test")


class TestRead(unittest.TestCase):
    def setUp(self):
        self.collection = "test"
        self.read_db = ReadFromDatabase("test", self.collection)
        data = {'TestWrite': 'pass'}

        self.write_db = WriteToDatabase("test", self.collection)
        self.write_db.add_data(data)


    def test_read_raw_data(self):
        self.assertTrue(self.read_db.read_raw_data() != None)

    def test_read_filtered_data(self):
        pass

    def tearDown(self):
        self.write_db.remove_collection(self.collection)

if __name__ == '__main__':
    unittest.main()
