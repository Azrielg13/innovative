import os
from agent import Agent

gpt_model = "gpt-4.1"
deepseek_model = "deepseek-r1:7b"
model = gpt_model

if model.startswith("gpt"):
  from langchain_openai import ChatOpenAI
  from dotenv import load_dotenv
  load_dotenv()
  api_key = os.environ.get("OPENAI_API_KEY")
  if api_key is None:
    print("Need to create an .env file and put OPENAI_API_KEY=[OPENAI_API_KEY]")
    print("For GCP create env_variables.yaml and follow instructions at "
          "https://stackoverflow.com/questions/22669528")
  llm = ChatOpenAI(model=model, api_key=api_key, use_responses_api=True)
else:
  from langchain_ollama import ChatOllama
  llm = ChatOllama(model=model)

llm.temperature = 0
llm_with_tools = llm.bind_tools([
  {
    "type": "mcp",
    "server_label": "ip360-mcp-server",
    "server_url": "https://ip360-mcp-server-627195764406.us-central1.run.app/mcp",
    "require_approval": "never",
  }
])

class MCPAgent(Agent):
  def __init__(self, llm=None, system=None, ip_address=None, creation_time=None, last_modified_time=None, messages=None):
    super().__init__(llm or llm_with_tools, system, ip_address, creation_time, last_modified_time, messages)

  def __call__(self, question):
    answer = self.execute(question)[-1]
    self.messages.append({"role": 'user', "content": question})
    self.messages.append({"role": 'assistant', "content": answer['text']})
    self.trim_messages()
    return answer


if __name__ == "__main__":
  with open('../mcp/token.id', 'r') as f:
    id_token = f.readline()

    agent = MCPAgent(system=f"idToken={id_token}")
    print(agent("Fetch appointment 6203642319208448"))
    print(agent("What patients are there named Decan?"))
    print(agent("What nurses do we have that live in Corona?"))
