require 'test_helper'

class RelationshipsInterfaceTest < ActionDispatch::IntegrationTest
  def setup
    @user = users(:michael)
  end

  test "count relationships" do
    log_in_as(@user)
    get root_path
    assert_match "#{@user.active_relationships.count}", response.body
    assert_match "#{@user.passive_relationships.count}", response.body
  end
end
