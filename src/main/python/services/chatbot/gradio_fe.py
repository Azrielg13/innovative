import gradio
from dotenv import load_dotenv
load_dotenv()
from mcp_chat import MCPAgent

with open('../mcp/token.id', 'r') as f:
  id_token = f.readline()
  agent = MCPAgent(system=f"ipToken={id_token}")


def chat_func(question, history):
  return agent(question)


if __name__ == "__main__":
  # Set up the Gradio chat interface
  iface = gradio.ChatInterface(
    fn=chat_func,
    title="IP360 AI Chat",
    description="This interface allows you to chat with IP360 OS",
    theme="default")

  iface.launch(share=False)