class UsersController < ApplicationController

  def show
    @user = User.find(params[:id])
  end
  
  def new
    @user = User.new
  end

  def create
    @user = User.new(params[:user]) # Rails 4.0 以降ではエラーとなる
    if @user.save
      # success
    else
      render 'new'
    end
  end
end
