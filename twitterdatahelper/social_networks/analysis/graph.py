# from graph_tool.all import *
import graph_tool.all as gt
import social_networks.database_controller as db

reader = db.ReadFromDatabase("data", "congress_map_trimmed")
cursor = reader.read_raw_data()

g = gt.Graph()
g.add_vertex(cursor.count())

#testing
# for user in cursor:
#     print(user)
# cursor.rewind()

#add username property map
name_prop = g.new_vertex_property("string")
g.vertex_properties['name'] = name_prop

#add names to each vertex
for v in g.vertices():
    g.vp.name[v] = cursor[g.vertex_index[v]]['user']
    #print(g.vertex_properties['name'][v])

cursor.rewind()

#create all edges
for user in cursor:
    #v1 is the vertex where name = cursor['user']
    v1 = gt.find_vertex(g, g.vp.name, user['user'])[0]
    for mention in user['user_mentions']:
        try:
            # v2 is the vertex where name = mention
            v2 = gt.find_vertex(g, g.vp.name, mention)[0]
        except IndexError:
            print("Error: " + mention + " is not in the collection")
            continue

        if g.vp.name[v1] != g.vp.name[v2]:
            print("adding edge between " + g.vp.name[v1] + " and " + g.vp.name[v2])
            edge = g.add_edge(v1, v2)

#attempt at weighting the graph
pos = gt.sfdp_layout(g)

# state = gt.minimize_blockmodel_dl(g)
state = gt.minimize_nested_blockmodel_dl(g)

state.draw(
    pos=pos,
    vertex_text=g.vertex_index,
    vertex_font_size=12,
    output_size=(1000, 1000),
    output="graphs/pls_work.png")

# graph_draw(g, pos=pos, vertex_text=g.vertex_index, vertex_font_size=12,
#             output_size=(1000, 1000), output="graphs/new-graph.png")



